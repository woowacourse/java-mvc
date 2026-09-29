package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                for (Method method : controllerClass.getDeclaredMethods()) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    if (mapping == null) {
                        continue;
                    }

                    for (RequestMethod requestMethod : mapping.method()) {
                        HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                        handlerExecutions.put(key, new HandlerExecution(controller, method));
                    }
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
