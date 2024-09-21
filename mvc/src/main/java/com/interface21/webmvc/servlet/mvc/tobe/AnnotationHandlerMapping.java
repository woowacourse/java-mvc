package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<Controller> CONTROLLER_TYPE = Controller.class;
    private static final Class<RequestMapping> REQUEST_MAPPING_MARKER = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CONTROLLER_TYPE);

        for (Class<?> clazz : classes) {
            Object instance = instantiate(clazz);
            List<Method> handlerMethods = getRequestMappingMethods(clazz);
            handlerMethods.forEach(method -> putAllMatchedHandlerExecutionOf(instance, method));
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("컨트롤러를 인스턴스화 할 수 없습니다.: " + clazz, e);
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_MARKER))
                .toList();
    }

    private void putAllMatchedHandlerExecutionOf(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_MARKER);
        String path = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (hasNoHttpMethods(requestMethods)) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        }
    }

    private boolean hasNoHttpMethods(RequestMethod[] requestMethods) {
        return requestMethods.length == 0;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.from(request);
        return Optional.ofNullable(handlerExecutions.get(handlerKey))
                .orElseThrow(() -> new IllegalArgumentException(handlerKey + "과(와) 매칭되는 핸들러를 찾을 수 없습니다."));
    }
}
