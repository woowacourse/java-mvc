package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.Handler;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandler;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        validateHandlerRequest(request);
        String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return new ManualHandler(controllers.get(requestURI));
    }

    private void validateHandlerRequest(HttpServletRequest request) {
        if (!canHandle(request)) {
            log.error("ManualHandler가 처리할 수 있는 요청이 아닙니다. url = {}, method = {}", request.getRequestURI(), request.getMethod());
            throw new IllegalStateException("처리할 수 있는 핸들러가 없습니다.");
        }
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return controllers.containsKey(request.getRequestURI());
    }
}
