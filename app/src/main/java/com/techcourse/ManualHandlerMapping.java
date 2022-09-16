package com.techcourse;

import com.techcourse.controller.*;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.controller.tobe.HandlerExecution;
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
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        if (!controllers.containsKey(requestURI)) {
            return null;
        }
        final var controller = controllers.get(requestURI);
        return new HandlerExecution(controller, getExecute(controller));
    }

    private Method getExecute(Controller controller) {
        try {
            final var clazz = controller.getClass();
            return clazz.getMethod("execute");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }
}
