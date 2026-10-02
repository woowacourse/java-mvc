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
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections samples = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = samples.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            registerHandlers(aClass);
        }
    }

    private void registerHandlers(Class<?> controllerClass) {
        Method[] declaredMethods = controllerClass.getDeclaredMethods();
        Object controller = createController(controllerClass);
        for (Method declaredMethod : declaredMethods) {
            RequestMapping mapping = declaredMethod.getAnnotation(RequestMapping.class);
            if (mapping == null) {
                continue;
            }
            String url = mapping.value();
            RequestMethod[] method = mapping.method();
            if (method.length == 0) {
                method = RequestMethod.values();
            }
            for (RequestMethod requestMethod : method) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(controller, declaredMethod));
            }
        }
    }

    private static Object createController(Class<?> aClass) {
        Object controller;
        try {
            controller = aClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + aClass.getName(), e);
        }
        return controller;
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
