package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMappingAdapter.class);

    private final ManualHandlerMapping manualHandlerMapping;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public ManualHandlerMappingAdapter(final ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) throws Exception {
        final Controller controller = manualHandlerMapping.getHandler(request.getRequestURI());
        if (Objects.isNull(controller)) {
            return null;
        }
        final HandlerKey handlerKey = createHandlerKey(request);
        if (!handlerExecutions.containsKey(handlerKey)) {
            final HandlerExecution handlerExecution = createHandlerExecution(controller);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info(handlerKey.toString());
        }
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey createHandlerKey(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        return new HandlerKey(url, requestMethod);
    }

    private HandlerExecution createHandlerExecution(final Controller controller) throws Exception {
        Object controllerInstance;
        if (controller instanceof ForwardController) {
            controllerInstance = controller.getClass().getDeclaredConstructor(String.class).newInstance("/index.jsp");
        } else {
            controllerInstance = controller.getClass().getDeclaredConstructor().newInstance();
        }
        final Method method = controllerInstance.getClass().getMethod("execute");
        return new HandlerExecution(controllerInstance, method);
    }
}
