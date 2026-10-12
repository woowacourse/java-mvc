package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            log.info("Controller Name {} ", controllerClass.getName());
            Object controller = entry.getValue();

            for (Method method : ReflectionUtils.getAllMethods(
                    controllerClass,
                    ReflectionUtils.withAnnotation(RequestMapping.class)
            )) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                HandlerExecution handlerExecution = new HandlerExecution(controller, method);

                String path = mapping.value();

                RequestMethod[] requestMethods = mapping.method();
                if (requestMethods.length == 0) {
                    requestMethods = RequestMethod.values();
                }

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(path, requestMethod);

                    if (handlerExecutions.containsKey(handlerKey)) {
                        throw new IllegalStateException(
                                "중복된 핸들러 매핑입니다: " + path + " " + requestMethod
                        );
                    }

                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }

    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final Object handler = handlerExecutions.get(new HandlerKey(requestUri, requestMethod));

        if (handler != null) {
            return handler;
        }

        for (RequestMethod method : RequestMethod.values()) {
            if (handlerExecutions.containsKey(new HandlerKey(requestUri, method))) {
                throw new IllegalArgumentException(
                        "지원하지 않는 HTTP 메서드입니다: " + request.getMethod() + " " + requestUri
                );
            }
        }
        return null;
    }
}
