package com.interface21.webmvc.servlet.mvc.asis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ControllerHandlerMapping.class);
    private static final String VIEW_CONTROLLER_NAME_RULE = "View";
    private static final String CONTROLLER_POSTFIX_RULE = "Controller$";

    private final Object[] basePackage;
    private final Map<HandlerKey, Controller> controllers;

    public ControllerHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.controllers = new HashMap<>();
    }

    @Override
    public void initialize() {
        try {
            processControllers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Initialized ControllerHandlerMapping!");
    }

    private void processControllers() throws Exception {
        for (Class<?> controller : findControllers()) {
            processController(controller);
        }
    }

    private Set<Class<? extends Controller>> findControllers() {
        Reflections reflections = new Reflections(basePackage);

        return reflections.getSubTypesOf(Controller.class);
    }

    private void processController(Class<?> controller) throws Exception {
        Controller controllerInstance = (Controller) controller.getDeclaredConstructor().newInstance();
        HandlerKey handlerKey = createHandlerKey(controller);

        controllers.put(handlerKey, controllerInstance);
    }

    private HandlerKey createHandlerKey(Class<?> controller) {
        String controllerName = controller.getSimpleName();
        String url = resolveUrl(controllerName);
        RequestMethod requestMethod = resolveRequestMethod(controllerName);

        return new HandlerKey(url, requestMethod);
    }

    private String resolveUrl(String controllerName) {
        String targetName = controllerName.replaceFirst(CONTROLLER_POSTFIX_RULE, "");

        return targetName.replaceAll("([A-Z])", "/$1").toLowerCase();
    }

    private RequestMethod resolveRequestMethod(String controllerName) {
        if (controllerName.contains(VIEW_CONTROLLER_NAME_RULE)) {
            return RequestMethod.GET;
        }

        return RequestMethod.POST;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return controllers.get(handlerKey);
    }
}
