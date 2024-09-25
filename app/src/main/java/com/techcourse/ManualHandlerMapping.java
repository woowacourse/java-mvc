package com.techcourse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;

public class ManualHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<String, Object> handlers;

    public ManualHandlerMapping() {
        this.handlers = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized ManualHandlerMapping!");

        handlers.put("/", ForwardController.class);
        handlers.put("/login", LoginController.class);
        handlers.put("/login/view", LoginViewController.class);
        handlers.put("/logout", LogoutController.class);
        handlers.put("/register/view", RegisterViewController.class);
        handlers.put("/register", RegisterController.class);

        handlers.forEach((key, value) -> log.info("HandlerKey : {}, handler : {}", key, value));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);

        return handlers.get(requestURI);
    }
}
