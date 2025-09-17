package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner();
        Set<Class<?>> controllerClasses = controllerScanner.scan(basePackage);
        Map<Class<?>, Object> controllers = ReflectionUtils.newInstances(controllerClasses);

        for (Class<?> controllerClass : controllerClasses) {
            Object controller = controllers.get(controllerClass);
            List<Method> handlers = ReflectionUtils.getAllMethods(controllerClass, RequestMapping.class);

            for (Method handler : handlers) {
                registerHandlerExecution(handler, controller);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecution(Method handler, Object controller) {
        RequestMapping handlerAnnotation = handler.getAnnotation(RequestMapping.class);
        String url = handlerAnnotation.value();
        RequestMethod[] httpMethods = handlerAnnotation.method();

        for (RequestMethod httpMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(url, httpMethod);

            HandlerExecution handlerExecution = new HandlerExecution(controller, handler);

            log.info("Path : {}, Method : {} ,Controller : {}", url, httpMethod.name(),
                    handler.getDeclaringClass().getCanonicalName());
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerAdapter getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, httpMethod);
        return handlerExecutions.get(handlerKey);
    }
}
