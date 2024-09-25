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

    @Override
    public void initialize() {
        log.info("Initialized ManualHandlerMapping!");

        handlers.put("/", new ForwardController("/index.jsp"));
        handlers.put("/login", new LoginController());
        handlers.put("/login/view", new LoginViewController());
        handlers.put("/logout", new LogoutController());
        handlers.put("/register/view", new RegisterViewController());
        handlers.put("/register", new RegisterController());

        handlers.forEach((key, value) -> log.info("HandlerKey : {}, handler : {}", key, value));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);

        return handlers.get(requestURI);
    }
}
