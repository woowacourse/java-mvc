package com.techcourse;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.ManualHandler;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Handler> handlers = new HashMap<>();

    @Override
    public void initialize() {
        handlers.put("/", createHandler(new ForwardController("/index.jsp")));
        handlers.put("/login", createHandler(new LoginController()));
        handlers.put("/login/view", createHandler(new LoginViewController()));
        handlers.put("/logout", createHandler(new LogoutController()));
        handlers.put("/register/view", createHandler(new RegisterViewController()));

        handlers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, handlers.get(path).getClass()));
        log.info("Initialized Handler Mapping!");
    }

    private Handler createHandler(final Controller controller) {
        return new ManualHandler(controller);
    }

    @Override
    public boolean supports(final HttpServletRequest request) {
        return handlers.containsKey(request.getRequestURI());
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        log.debug("Request Mapping Uri : {}", requestURI);
        return handlers.get(requestURI);
    }
}
