package com.techcourse;

import com.techcourse.controller.*;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();

    @Override
    public void initialize() {
        CONTROLLERS.put("/", new ForwardController("/index.jsp"));
        CONTROLLERS.put("/login", new LoginController());
        CONTROLLERS.put("/login/view", new LoginViewController());
        CONTROLLERS.put("/logout", new LogoutController());

        LOG.info("Initialized Handler Mapping!");
        CONTROLLERS.forEach((path, controller) -> LOG.info("Path : {}, Controller : {}", path, controller.getClass()));
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        LOG.debug("Request Mapping Uri : {}", requestURI);

        return CONTROLLERS.get(requestURI);
    }
}
