package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    // HandlerKey(URI + HTTP Method) → HandlerExecution(실행 대상 객체 + 메서드) 매핑 저장소
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    // 스캔할 base package 목록
    private final Object[] basePackages;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.handlerExecutions = new HashMap<>();
        this.basePackages = basePackages;
    }

    /**
     * 초기화 메서드: handlerExecutions에 매핑 정보를 등록
     */
    public void initialize() {
        try {
            handlerExecutions.putAll(initHandlerExecutions());
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            log.error("error to initialize AnnotationHandlerMapping");
            throw new RuntimeException(e);
        }
    }

    /**
     * 조회 메서드: 요청(HttpServletRequest)에 맞는 핸들러(HandlerExecution)를 조회
     */
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }

    private Map<HandlerKey, HandlerExecution> initHandlerExecutions() {
        ControllerScanner controllerScanner = ControllerScanner.from(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        return createHandlerExecutions(controllers);
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(Map<Class<?>, Object> controllers) {

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (Class<?> controllerClass : controllers.keySet()) { // 컨트롤러 클래스 별
            Set<Method> methods = getRequestMappingMethods(controllerClass); // 클래스 안의 모든 메서드 가져오기

            for (Method method : methods) { // 컨트롤러 클래스 메서드별
                Object instance = controllers.get(controllerClass);
                handlerExecutions.putAll(createMethodHandlers(instance, method)); // 메서드 HandlerExecutions 생성
            }
        }

        return handlerExecutions;
    }

    @SuppressWarnings("unchecked")
    // 제네릭 타입 추론이 완전하지 않아, 내부적으로 raw type(Set)으로 반환해 Set<Method>로 강제로 맞출 때 unchecked cast 경고 발생
    // withAnnotation(RequestMapping.class) 조건을 줌으로써 반환되는 건 무조건 Method라 캐스팅이 안전하므로
    // SuppressWarnings를 활용해 경고를 숨김
    private Set<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)); // 컨트롤러에서 RequestMapping이 붙어 있는 메서드 정보를 가져온다.
    }

    private Map<HandlerKey, HandlerExecution> createMethodHandlers(final Object instance, final Method method) {
        var requestMapping = method.getDeclaredAnnotation(RequestMapping.class);      // @RequestMapping 읽기
        var requestMethods = requestMapping.method();                                 // 지원하는 HTTP Method 배열

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (RequestMethod requestMethod : requestMethods) {
            // RequestMapping 으로 부터 HandlerKey 객체에 URL과 HTTP method를 저장한다.
            var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            // handlerExecution는 실행할 메서드의 인스턴스와 실행할 메서드를 인스턴스 변수로 갖는다.
            var handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }

        return handlerExecutions;
    }
}
