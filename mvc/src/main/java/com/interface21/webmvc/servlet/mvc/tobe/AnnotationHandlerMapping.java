package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping() {
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize(List<Object> controllers) {
        log.info("Initialized AnnotationHandlerMapping!");
        controllers.forEach(controller -> {
            log.info("Controller: {}", controller.getClass().getName());
            initHandlerExecutions(controller);
        });
    }

    private void initHandlerExecutions(final Object controller) {
        final Method[] methods = controller.getClass().getDeclaredMethods();
        for (var method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());

                HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                    throw new IllegalStateException("중복된 요청 매핑입니다: " + handlerKey);
                }
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
