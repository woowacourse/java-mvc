package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
-- 어노테이션을 읽어서 [경로] + [메서드] => [실행 대상] 관계를 등록/조회한다.
-- 조회된 [실행 대상], 컨트롤러 메서드의 실행 자체는 HandlerExecution 의 책임.
 */
public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    // 조회의 범위. 어느 패키지들을 조회할 것인지. reflections 의 파라미터로 해당 범위에 속한 클래스를 조회한다.
    private final Object[] basePackage;

    // HandlerKey - 요청의 경로와 HTTP 메서드를 관리. 예: HandlerKey("/get-test", GET)
    // HandlerExecution - 실행할 컨트롤러 객체와 HTTP 메서드에 대응(해서 실행되)하는 핸들러를 관리. 예: TestController, findUserId
    // HandlerExecutions - 요청의 경로/HTTP메서드, 실행할 컨트롤러 객체/메서드의 핸들러(method) 정보를 관리.
    private Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
//        패키지 경로들을 순회하면서
        for (Object basePath : basePackage) {
//            해당 경로의 리플렉션을 생성하고
            Reflections reflections = new Reflections(basePath);
//            리플렉션으로 @Controller 어노테이션이 붙은 모든 클래스를 Set 으로 가져오고
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
//            그 `컨트롤러 클래스`로 `컨트롤러 인스턴스 객체`를 생성해 Key-Value 로 매핑한다
            Map<Class<?>, Object> controllers = generateControllers(controllerClasses);
//            매핑된 클래스/객체 맵을 순회하며 `등록` 한다
            controllers.forEach(this::registerControllers);
        }
    }

    private Map<Class<?>, Object> generateControllers(Set<Class<?>> controllers) {
//        깔끔은 한데 난해함
//        return controllers.stream()
//                .collect(Collectors.toMap(
//                        controller -> controller,
//                        this::instantiateController
//                ));
        Map<Class<?>, Object> controllerMap = new HashMap<>();
//        컨트롤러 클래스들을 순회하며
        for (Class<?> controller : controllers) {
//            해당 클래스로 컨트롤러 인스턴스 객체를 생성하고
            Object instance = instantiateController(controller);
//            클래스를 Key, 인스턴스 객체를 Value 로 저장
            controllerMap.put(controller, instance);
        }
//        모든 컨트롤러 클래스를 순회하고 맵을 반환
        return controllerMap;
    }

    private Object instantiateController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
//            만약 해당 클래스에 NoArgsConstructor 가 없으면 예외
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controller.getName(), e);
        }
    }

    private void registerControllers(Class<?> controllerClass, Object controller) {
        /*
          @RequestMapping(value = "/get-test", method = RequestMethod.GET)
         */
//        해당 클래스의 모든 메서드를 순회하며
        Arrays.stream(controllerClass.getMethods())
//                @RequestMapping 어노테이션이 붙은 메서드를 골라내
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
//                해당 메서드와 클래스 인스턴스 객체를 넘겨 핸들러를 등록
                .forEach(method -> registerHandler(controller, method));
    }

    private void registerHandler(Object controller, Method method) {
//        핸들러 실행 객체를 생성,
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
//        메서드에 붙어있던 어노테이션 객체를 꺼내 와
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

//        RequestMapping 어노테이션에 붙어있는 모든 Request 메서드를 순회하며
        Arrays.stream(getSupportedMethods(requestMapping))
//                메서드, 어노테이션 객체, 핸들러 실행 객체를 넘겨 핸들러를 등록한다.
                .forEach(requestMethods -> registerRequestHandlerOrThrow(
                        requestMethods, requestMapping, handlerExecution));
    }

    private RequestMethod[] getSupportedMethods(RequestMapping requestMapping) {
        RequestMethod[] method = requestMapping.method();
        if (method.length == 0) {
            return RequestMethod.values();
        }
        return method;
    }

    private void registerRequestHandlerOrThrow(
            RequestMethod requestMethods, RequestMapping requestMapping, HandlerExecution handlerExecution) {
//        ↑ HandlerKey - 요청의 경로와 HTTP 메서드를 관리. 예: HandlerKey("/get-test", GET)
//        요청된 경로, Uri와 요청된 메서드로 HandlerKey를 생성
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethods);
//        ↑ HandlerExecutions - 실행할 컨트롤러 객체와 Method 정보를 관리.
//        HandlerKey로 등록된 핸들러 실행 객체가 있다면 예외, 없으면 등록.
        if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
            throw new IllegalStateException("Duplicate handler key: " + handlerKey);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
//        경로, 메서드, 인스턴스 객체, 핸들러를 등록의 역순으로 꺼낸다.
        RequestMethod requestMethod;
        try {
//            리퀘스트의 메서드가 유효한 값이면 파싱
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
//            유효하지 않으면 null 반환
            return null;
        }

//        요청된 경로와 요청 메서드로 HandlerKey 생성
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
//        생성된 HandlerKey 로 핸들러 실행 객체 조회해 반환
        return handlerExecutions.get(handlerKey);
    }
}
