package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        // handlerExecutions reflection 이용해서 초기화한다.
        for (Object o : basePackage) { // basePackage 를 전부 뒤져서 Controller 를 등록한다.
            String path = (String) o;
            Reflections reflections = new Reflections(path); // 해당 경로를 뒤질 리플랙션을 생성한다.
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class); // Controller 애너테이션이 붙은 컨트롤러들을 전부 꺼낸다.
            for (Class<?> controller : controllers) { // 컨트롤러들을 전부 순회한다.
                log.debug("Controller = {}\n", controller);
                Method[] methods = controller.getDeclaredMethods(); // 컨트롤러에 선언된 모든 메서드들을 꺼낸다.
                for (Method method : methods) { // 컨트롤러의 모든 메서드들을 순회한다.
                    log.debug("Method = {}", method);
                    Annotation[] annotations = method.getAnnotations(); // 컨트롤러의 메서드에 선언된 모든 애너테이션을 꺼낸다.
                    for (Annotation annotation : annotations) { // 컨트롤러의 메서드에 선언된 모든 애너테이션을 순회한다.
                        if (annotation instanceof RequestMapping requestMapping) { // 애너테이션이 RequestMapping 이면 형변환한다.
                            if (requestMapping.method().length == 0) {
                                for (RequestMethod requestMethod : RequestMethod.values()) {
                                    createHandlerKeyAndExecution(requestMapping, requestMethod, controller, method);
                                }
                                return;
                            }
                            RequestMethod requestMethod = requestMapping.method()[0]; // RequestMapping 의 method 를 꺼낸다.
                            createHandlerKeyAndExecution(requestMapping, requestMethod, controller, method);
                        }
                    }
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void createHandlerKeyAndExecution(RequestMapping requestMapping, RequestMethod requestMethod,
                                              Class<?> controller, Method method) {
        String url = requestMapping.value(); // RequestMapping 의 url 을 꺼낸다.
        HandlerKey handlerKey = new HandlerKey(url, requestMethod); // url, method 를 HandlerKey 로 만든다.
        HandlerExecution handlerExecution = new HandlerExecution(controller, method); // HandlerExecution 을 만든다.

        log.debug("requestMapping = {}", requestMapping);
        log.debug("handlerKey = {}\n", handlerKey);

        handlerExecutions.put(handlerKey, handlerExecution); // HandlerKey 에 해당하는 HandlerExecution 을 맵에 저장한다.
    }

    public Object getHandler(final HttpServletRequest request) {
        // request 에 따라서 HandlerExecution 을 반환해야함.
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }
}
