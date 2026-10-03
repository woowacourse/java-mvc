package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            Object instance = createInstance(controller);

            for (Method method : controller.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    register(method, instance);
                }
            }
            log.info("찾은 컨트롤러: {}", controller.getName());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }


    private static Object createInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 인스턴스를 생성할 수 없습니다. : " + controller.getName(), e);
        }
    }

    private void register(Method method, Object instance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMethod.name()));
            handlerExecutions.put(key, new HandlerExecution(instance, method));
            log.info("등록: {}", key);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = String.valueOf(request.getRequestURI());
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
