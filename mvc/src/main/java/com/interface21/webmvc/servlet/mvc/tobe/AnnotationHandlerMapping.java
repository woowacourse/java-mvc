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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();

                for (Method method : controllerClass.getDeclaredMethods()) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    if (mapping == null) {
                        continue;
                    }

                    RequestMethod[] methods = mapping.method();
                    if (methods.length == 0) {
                        methods = RequestMethod.values();
                    }

                    for (RequestMethod requestMethod : methods) {
                        HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                        HandlerExecution execution = new HandlerExecution(controller, method);
                        HandlerExecution existingExecution = handlerExecutions.putIfAbsent(key, execution);
                        if (existingExecution != null) {
                            throw new IllegalStateException("Duplicate handler mapping for " + key + " found in "
                                    + controllerClass.getName() + "#" + method.getName());
                        }
                    }
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey key = new HandlerKey(url, method);
        return handlerExecutions.get(key);
    }
}
