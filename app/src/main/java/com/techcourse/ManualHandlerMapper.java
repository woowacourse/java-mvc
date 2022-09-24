package com.techcourse;

import com.techcourse.controller.v1.LoginV1Controller;
import com.techcourse.controller.v1.LoginViewV1Controller;
import com.techcourse.controller.v1.LogoutV1Controller;
import com.techcourse.controller.v1.RegisterV1Controller;
import com.techcourse.controller.v1.RegisterViewV1Controller;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handler.mapper.HandlerMapper;
import nextstep.mvc.handler.asis.Controller;
import nextstep.mvc.handler.asis.ForwardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapper.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginV1Controller());
        controllers.put("/login/view", new LoginViewV1Controller());
        controllers.put("/logout", new LogoutV1Controller());
        controllers.put("/register/view", new RegisterViewV1Controller());
        controllers.put("/register", new RegisterV1Controller());

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
