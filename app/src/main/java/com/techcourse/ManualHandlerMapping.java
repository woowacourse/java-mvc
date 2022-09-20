package com.techcourse;

import com.techcourse.controller.v1.LoginV1Controller;
import com.techcourse.controller.v1.LoginViewV1Controller;
import com.techcourse.controller.v1.LogoutV1Controller;
import com.techcourse.controller.v1.RegisterV1Controller;
import com.techcourse.controller.v1.RegisterViewV1Controller;
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
        controllers.put("/v1", new ForwardController("/index.jsp"));
        controllers.put("/v1/login", new LoginV1Controller());
        controllers.put("/v1/login/view", new LoginViewV1Controller());
        controllers.put("/v1/logout", new LogoutV1Controller());
        controllers.put("/v1/register/view", new RegisterViewV1Controller());
        controllers.put("/v1/register", new RegisterV1Controller());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
