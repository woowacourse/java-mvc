package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
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
        Set<Class<?>> controllerClasses =
                reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            Object controller;
            try {
                controller = controllerClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 생성 실패: " + controllerClass.getName(), e);
            }

            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    log.info("Mapping method: {}", method.getName());

                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    String url = mapping.value();
                    RequestMethod[] httpMethods = mapping.method();

                    if (httpMethods.length == 0) {
                        httpMethods = RequestMethod.values();
                    }

                    for (RequestMethod httpMethod : httpMethods) {
                        HandlerKey key = new HandlerKey(url, httpMethod);

                        if (handlerExecutions.containsKey(key)) {
                            throw new IllegalStateException("중복된 요청 매핑입니다: " + httpMethod + " " + url);
                        }

                        HandlerExecution execution = new HandlerExecution(controller, method);
                        handlerExecutions.put(key, execution);
                        log.info("Handler key: {}", key);
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, method);

        return handlerExecutions.get(key);
    }
}
