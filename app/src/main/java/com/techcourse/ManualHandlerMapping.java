package com.techcourse;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);
    private static final String IMPLEMENT_METHOD_NAME = "execute";

    private final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());

        log.info("Initialized Manual Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request.getRequestURI());
        Controller controller = controllers.getOrDefault(request.getRequestURI(), null);

        if (Objects.isNull(controller)) {
            return null;
        }

        Method execution = Arrays.stream(controller.getClass()
                .getMethods())
                .filter(method -> method.getName().equals(IMPLEMENT_METHOD_NAME))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Execute 메서드가 구현되어 있지 않습니다."));

        return new HandlerExecution(controller, execution);
    }

}
