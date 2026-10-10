package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {

            try {
                Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controllerClass);
                Object controller = constructor.newInstance();
                for (Method method : controllerClass.getMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String url = requestMapping.value();
                        RequestMethod[] requestMethods = readRequestMethods(requestMapping);

                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                            if (handlerExecutions.containsKey(handlerKey)) {
                                throw new IllegalStateException(
                                        "중복된 매핑입니다. key: %s, method: %s".formatted(handlerKey, method));
                            }
                            handlerExecutions.put(handlerKey, new HandlerExecution(method, controller));
                        }

                    }
                }

            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(controllerClass.getName() + " 생성 실패", e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            return handlerExecutions.get(new HandlerKey(url, requestMethod));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private RequestMethod[] readRequestMethods(RequestMapping requestMapping) {
        if(requestMapping.method().length == 0) {
            return RequestMethod.values();
        }

        return requestMapping.method();
    }
}
