package com.techcourse.air.mvc.core.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techcourse.air.core.annotation.Controller;
import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import jakarta.servlet.http.HttpServletRequest;
import com.techcourse.air.mvc.core.controller.tobe.HandlerExecution;
import com.techcourse.air.mvc.core.controller.tobe.HandlerKey;
import com.techcourse.air.mvc.web.annotation.RequestMapping;
import com.techcourse.air.mvc.web.support.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    public AnnotationHandlerMapping() {
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        List<Object> controllers = context.findAllBeanHasAnnotation(Controller.class);
        for (Object controller : controllers) {
            Class<?> clazz = controller.getClass();
            List<Method> allMethods = context.findAllMethodByAnnotation(clazz, RequestMapping.class);
            registerRequestMappingMethods(controller, allMethods);
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                         .forEach(key -> log.info("Path : {}, HandlerExecution : {}", key.toString(), handlerExecutions.get(key)
                                                                                                                       .toString()));
    }

    private void registerRequestMappingMethods(Object controller, List<Method> allMethods) {
        for (Method method : allMethods) {
            registerHandlerExecution(controller, method);
        }
    }

    private void registerHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String value = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();

        for (RequestMethod requestMethod : methods) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }
}
