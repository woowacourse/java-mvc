package com.techcourse;

import com.techcourse.controller.lagacy.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapper;

import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(LegacyHandlerMapper.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    public Controller getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.debug("Request Mapping URI : {}", requestURI);
        return controllers.get(requestURI);
    }
}
