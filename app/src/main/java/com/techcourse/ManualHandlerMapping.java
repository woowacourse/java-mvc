package com.techcourse;

import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

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

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean isMatch(final HttpServletRequest request) {
        return controllers.containsKey(request.getRequestURI());
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);

        final Controller controller = controllers.get(requestURI);
        final Method handlerMethod = getExecuteMethod(controller);

        return new HandlerExecution(handlerMethod, controller);
    }

    private Method getExecuteMethod(Controller controller) {
        final String methodName = "execute";
        final Class<?>[] paramTypes = {HttpServletRequest.class, HttpServletResponse.class};

        try {
            return controller.getClass().getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new HandlerNotFoundException(
                    "execute handler method를 찾을 수 없습니다. controller name: " + controller.getClass().getSimpleName()
            );
        }
    }
}
