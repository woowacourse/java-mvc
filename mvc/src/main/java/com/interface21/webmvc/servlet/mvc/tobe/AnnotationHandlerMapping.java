package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import javax.annotation.Nullable;
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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllers) {
            Object instance = createInstance(controllerClass);
            createHandlerExecutions(controllerClass, instance);

        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void createHandlerExecutions(Class<?> controllerClass, Object instance) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            RequestMapping mapping = getRequestMapping(method);
            if (mapping == null) {
                continue;
            }

            HandlerKey handlerKey = new HandlerKey(mapping.value(), mapping.method()[0]);
            handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        }
    }

    @Nullable
    private static RequestMapping getRequestMapping(Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return null;
        }
        return mapping;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(),
            RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + clazz.getName(), e);
        }
    }
}
