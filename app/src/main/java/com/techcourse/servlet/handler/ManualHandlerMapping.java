package com.techcourse.servlet.handler;

import com.techcourse.controller.LoginMyController;
import com.techcourse.controller.LoginViewMyController;
import com.techcourse.controller.LogoutMyController;
import com.techcourse.controller.RegisterMyController;
import com.techcourse.controller.RegisterViewMyController;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardMyController;
import webmvc.org.springframework.web.servlet.mvc.asis.MyController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, MyController> controllers = new HashMap<>();

    public void initialize() {
        controllers.put("/", new ForwardMyController("/index.jsp"));
        controllers.put("/login", new LoginMyController());
        controllers.put("/login/view", new LoginViewMyController());
        controllers.put("/logout", new LogoutMyController());
        controllers.put("/register/view", new RegisterViewMyController());
        controllers.put("/register", new RegisterMyController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request.getRequestURI());
        return controllers.get(request.getRequestURI());
    }
}
