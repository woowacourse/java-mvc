package com.techcourse;

import com.techcourse.controller.*;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.mapping.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());

        controllers.forEach((path, value) -> log.info("Path: [{}], Controller: [{}]", path, value.getClass()));
        log.info("Initialized Manual Handler Mapping!");
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.info("Request Manual Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
