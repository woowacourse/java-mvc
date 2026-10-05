package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.context.stereotype.Controller;

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

        for(Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {

            Object controller = createController(controllerClass);

            for (Method method : controllerClass.getDeclaredMethods()) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);

                if (mapping == null) {
                    continue;
                }

                HandlerExecution execution = new HandlerExecution(controller, method);

                RequestMethod[] requestMethods = mapping.method();

                if (requestMethods.length == 0) {
                    requestMethods = RequestMethod.values();
                }

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey key = new HandlerKey(mapping.value(), requestMethod);

                    if (handlerExecutions.containsKey(key)) {
                        throw new IllegalStateException("중복된 매핑 요청 : " + key);
                    }

                    handlerExecutions.put(key, execution);
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);

        return handlerExecutions.get(key);
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + controllerClass.getName(), e);
        }
    }
}
