package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : typesAnnotatedWith) {
            log.info("Controller Name {} ", controllerClass.getName());
            Object controller = createController(controllerClass);

            for (Method declaredMethod : controllerClass.getDeclaredMethods()) {
                RequestMapping mapping = declaredMethod.getAnnotation(RequestMapping.class);

                if (mapping == null) {
                    continue;
                }

                String path = mapping.value();

                RequestMethod[] requestMethods = mapping.method();
                if (requestMethods.length == 0) {
                    requestMethods = RequestMethod.values();
                }

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(path, requestMethod);

                    handlerExecutions.put(handlerKey, new HandlerExecution(controller, declaredMethod));
                }
            }
        }

    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private Object createController(Class<?> controllerClass) {
        try {
            Constructor<?> constructor = controllerClass.getDeclaredConstructor();

            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 객체 생성에 실패했습니다: " + controllerClass.getName(), e);
        }
    }
}
