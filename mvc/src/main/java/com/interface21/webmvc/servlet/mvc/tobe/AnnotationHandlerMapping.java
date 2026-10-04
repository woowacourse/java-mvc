package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {

            for (Method method : clazz.getMethods()) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);

                if (annotation != null) {
                    String url = annotation.value();
                    RequestMethod[] requestMethods = annotation.method();

                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                        Object controller = clazz.getDeclaredConstructor().newInstance();

                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        if (handlerExecutions.containsKey(handlerKey)){
                            throw new IllegalStateException("중복된 요청 매핑입니다: " + handlerKey);
                        }
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        return handlerExecutions.getOrDefault(handlerKey, null);
    }
}
