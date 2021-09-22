package com.techcourse;

import com.techcourse.controller.legacy.LegacyLoginController;
import com.techcourse.controller.legacy.LegacyLoginViewController;
import com.techcourse.controller.legacy.LegacyLogoutController;
import com.techcourse.controller.legacy.LegacyRegisterController;
import com.techcourse.controller.legacy.LegacyRegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
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
        controllers.put("/login", new LegacyLoginController());
        controllers.put("/login/view", new LegacyLoginViewController());
        controllers.put("/logout", new LegacyLogoutController());
        controllers.put("/register/view", new LegacyRegisterViewController());
        controllers.put("/register", new LegacyRegisterController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet().forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
