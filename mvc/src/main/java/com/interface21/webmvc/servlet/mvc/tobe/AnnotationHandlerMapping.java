package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Reflections reflections;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.reflections = new Reflections(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        // handlerExecutions reflection 이용해서 초기화한다.
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(
                Controller.class); // Controller 애너테이션이 붙은 컨트롤러들을 전부 꺼낸다.
        controllers.forEach(this::processController); // 컨트롤러들을 전부 순회한다.
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void processController(Class<?> controller) {
        log.debug("Controller = {}\n", controller);
        Method[] methods = controller.getDeclaredMethods(); // 컨트롤러에 선언된 모든 메서드들을 꺼낸다.
        Arrays.stream(methods).forEach(this::processMethod); // 컨트롤러의 모든 메서드들을 순회한다.
    }

    private void processMethod(Method method) {
        log.debug("Method = {}\n", method);
        Annotation[] annotations = method.getAnnotations(); // 컨트롤러의 메서드에 선언된 모든 애너테이션을 꺼낸다.
        Arrays.stream(annotations) // 컨트롤러의 메서드에 선언된 모든 애너테이션을 순회한다.
                .filter(annotation -> annotation instanceof RequestMapping)
                .map(annotation -> (RequestMapping) annotation) // 애너테이션이 RequestMapping 이면 형변환한다.
                .forEach(requestMapping -> processRequestMapping(requestMapping, method));
    }

    private void processRequestMapping(RequestMapping requestMapping, Method method) {
        if (requestMapping.method().length == 0) { // RequestMapping 에 RequestMethod 가 없으면
            Arrays.stream(RequestMethod.values()) // 모든 RequestMethod 를 지원한다.
                    .forEach(requestMethod -> createHandlerKeyAndExecution(requestMapping, requestMethod, method));
            return;
        }
        Arrays.stream(requestMapping.method())
                .forEach(requestMethod -> createHandlerKeyAndExecution(requestMapping, requestMethod, method));
    }

    private void createHandlerKeyAndExecution(RequestMapping requestMapping,
                                              RequestMethod requestMethod,
                                              Method method) {
        String url = requestMapping.value(); // RequestMapping 의 url 을 꺼낸다.
        HandlerKey handlerKey = new HandlerKey(url, requestMethod); // url, method 를 HandlerKey 로 만든다.
        log.debug("requestMapping = {}", requestMapping);
        log.debug("handlerKey = {}\n", handlerKey);
        try {
            HandlerExecution handlerExecution = new HandlerExecution(method); // HandlerExecution 을 만든다.
            handlerExecutions.put(handlerKey, handlerExecution); // HandlerKey 에 해당하는 HandlerExecution 을 맵에 저장한다.
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        // request 에 따라서 HandlerExecution 을 반환해야함.
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        log.debug("handlerKey = {}", handlerKey);

        return handlerExecutions.get(handlerKey);
    }
}
