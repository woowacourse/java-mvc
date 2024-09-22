package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
        for (Object basePackage : basePackage) {
            Reflections reflections = new Reflections((String) basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            try {
                initializeControllers(controllers);
            } catch (Exception e) {
                log.error("Fail to initialized AnnotationHandlerMapping", e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeControllers(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            try {
                initializeMethods(controller.getDeclaredConstructor().newInstance(), controller.getMethods());
            } catch (Exception e) {
                log.error("Handler의 Method를 매핑하는 과정에서 오류가 발생했습니다.", e);
            }
        }
    }

    private void initializeMethods(Object controller, Method[] methods) {
        for (Method method : methods) {
            initializeMethod(controller, method);
        }
    }

    private void initializeMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        String url = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        initializeHandlerExecution(controller, method, requestMethods, url);
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if(requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        return requestMethods;
    }

    private void initializeHandlerExecution(Object controller, Method method, RequestMethod[] requestMethods, String url) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
