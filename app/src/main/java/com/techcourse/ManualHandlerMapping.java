package com.techcourse;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMapping;
import com.techcourse.controller.RegisterViewController;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<String, Controller> controllers = new HashMap<>();
    private boolean initialized = false;

    public void initialize() {
        if (initialized) {
            log.warn("ManualHandlerMapping이 이미 초기화되었습니다.");
            return;
        }
        log.info("ManualHandlerMapping을 초기화했습니다.");

        controllers.putIfAbsent("/", new ForwardController("/index.jsp"));
        controllers.putIfAbsent("/register/view", new RegisterViewController());

        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));

        initialized = true;
    }


    public Object getHandler(final HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request.getRequestURI());
        return controllers.get(request.getRequestURI());
    }
}
