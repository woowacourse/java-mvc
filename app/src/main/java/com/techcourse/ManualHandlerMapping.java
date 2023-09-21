package com.techcourse;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.MannualHandler;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, MannualHandler> handlers = new HashMap<>();

    @Override
    public void initialize() {
        handlers.put("/", new MannualHandler(new ForwardController("/index.jsp")));
        handlers.put("/login", new MannualHandler(new LoginController()));
        handlers.put("/login/view", new MannualHandler(new LoginViewController()));
        handlers.put("/logout", new MannualHandler(new LogoutController()));

        log.info("Initialized Handler Mapping!");
        handlers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, handlers.get(path).getController().getClass()));
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return handlers.get(requestURI);
    }
}
