package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Object controller = createInstance(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String uri = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();

                    if (requestMethods.length == 0) {
                        //모든 메서드 지원해야함
                        requestMethods = RequestMethod.values();
                    }

                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

                        //요청을 실제 컨트롤러 메서드에 연결하고 실행해ㅐ야하니까,
                        //컨트롤러랑 메서드를 알야야함.
                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                    }
                }
            }
        }
    }

    private Object createInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.info("인스턴스 생성에 실패했습니다. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
