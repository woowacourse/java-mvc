package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 어노테이션을 읽어 "요청 경로 + HTTP 메서드 → 실행 대상" 관계를 등록하고 조회한다.
 * 컨트롤러 메서드의 실행 자체는 HandlerExecution의 역할이다.
 *
 * 요청을 받기 전에 initialize()를 완료하고, 요청 처리 중에는 조회만 하는 사용을 전제로 한다.
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    // 탐색 범위 설정. 예: new AnnotationHandlerMapping("samples", "another.package")
    // Object...는 가변 인자이며, 전달된 설정들은 Object[]로 보관된다.
    private final Object[] basePackage;

    // key: 요청을 구분하는 경로와 HTTP 메서드. 예: HandlerKey("/get-test", GET)
    // value: 실행할 컨트롤러 객체와 Method 정보를 묶은 HandlerExecution
    // final은 Map 참조를 바꾸지 못하게 할 뿐, Map에 항목을 추가하거나 지우는 것은 가능하다.
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        // @Controller 클래스를 찾고 객체를 생성한다.
        // 결과 예: TestController.class → 생성된 TestController 객체
        final var controllers = new ControllerScanner(basePackage).getControllers();

        // 초기화를 다시 호출하더라도 이전 등록 내용을 남기지 않고 새로 구성한다.
        // HashMap을 수정하므로 요청 조회와 동시에 initialize()를 호출해서는 안 된다.
        handlerExecutions.clear();

        // Map.forEach는 key와 value를 함께 전달한다.
        // 아래 코드는 controllers.forEach((type, instance) -> registerController(type, instance))와 같다.
        controllers.forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        // Class는 메서드 목록을 조사할 때, controller 객체는 나중에 그 메서드를 실행할 때 필요하다.
        // getMethods()는 상속받은 것을 포함한 공개 메서드들을 반환한다.
        Arrays.stream(controllerClass.getMethods())
                // Object의 toString() 등도 목록에 있지만, 어노테이션이 없으면 여기서 제외된다.
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                // 어노테이션이 있는 각각의 메서드를 요청 매핑에 등록한다. 아직 실행하지 않는다.
                .forEach(method -> registerHandler(controller, method));
    }

    private void registerHandler(final Object controller, final Method method) {
        // 예: @RequestMapping(value = "/get-test", method = RequestMethod.GET)의 설정을 읽는다.
        final var requestMapping = method.getAnnotation(RequestMapping.class);

        // "어느 객체의 어느 메서드인가"를 함께 보관한다. 생성만 하고 메서드는 호출하지 않는다.
        final var handlerExecution = new HandlerExecution(controller, method);

        // method 설정은 배열이다. GET과 POST가 지정되었다면 각각 별도의 key로 등록한다.
        // 이때 두 key의 value는 동일한 handlerExecution 객체다.
        Arrays.stream(getSupportedMethods(requestMapping))
                .forEach(requestMethod -> {
                    // value()는 어노테이션에 적힌 경로 문자열을 반환한다.
                    final var key = new HandlerKey(requestMapping.value(), requestMethod);

                    // key가 없으면 등록하고 null을 반환한다.
                    // 이미 있으면 덮어쓰지 않고 기존 value를 반환하므로 중복을 감지할 수 있다.
                    if (handlerExecutions.putIfAbsent(key, handlerExecution) != null) {
                        throw new IllegalStateException("중복된 요청 매핑입니다: " + key);
                    }
                });
    }

    private RequestMethod[] getSupportedMethods(final RequestMapping requestMapping) {
        final var methods = requestMapping.method();
        // @RequestMapping의 method 기본값은 빈 배열이다.
        // 생략된 경우에는 RequestMethod enum에 정의된 모든 HTTP 메서드에 등록한다.
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        // 명시된 경우에는 지정한 HTTP 메서드들만 지원한다.
        return methods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            // 요청의 "GET" 문자열을 enum 값 RequestMethod.GET으로 변환한다.
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            // 예: enum에 없는 "CONNECT"라면 변환에 실패한다. 실행 대상이 없다는 의미로 null을 반환한다.
            return null;
        }

        // 등록할 때와 동일한 형태로 조회 key를 만든다. 예: ("/get-test", GET)
        // 새로운 key 객체여도 equals/hashCode가 경로와 HTTP 메서드를 비교하므로 조회할 수 있다.
        final var key = new HandlerKey(request.getRequestURI(), requestMethod);

        // 일치하는 HandlerExecution을 반환한다. 없으면 Map.get()이 null을 반환한다.
        // 반환 타입은 제공된 뼈대의 Object를 유지했지만, 등록된 실제 value 타입은 HandlerExecution이다.
        return handlerExecutions.get(key);
    }
}
