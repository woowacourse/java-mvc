package com.techcourse;

import com.interface21.NotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public ManualHandlerMapping() {
        initialize();
    }

    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (!controllers.containsKey(requestURI)) {
            throw new NotFoundException("일치하는 uri가 없습니다");
        }
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
