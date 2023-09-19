package com.techcourse;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

public class ManualHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, ManualHandlerExecution> controllers = new HashMap<>();

    public void initialize() {
        controllers.put("/", new ManualHandlerExecution(new ForwardController("/index.jsp")));
        controllers.put("/login", new ManualHandlerExecution(new LoginController()));
        controllers.put("/login/view", new ManualHandlerExecution(new LoginViewController()));
        controllers.put("/logout", new ManualHandlerExecution(new LogoutController()));

        log.info("Initialized Manual Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    public HandlerExecution getHandler(String requestURI) {
        log.debug("Request Mapping Uri : {}", requestURI);

        return controllers.get(requestURI);
    }
}
