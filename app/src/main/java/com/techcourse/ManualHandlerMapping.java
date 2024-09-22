package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final ConcurrentMap<String, ManualHandler> handlers;

    public ManualHandlerMapping(ConcurrentMap<String, ManualHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void initialize() {
        handlers.put("/", new ManualHandler(new ForwardController("/index.jsp")));
        handlers.put("/login", new ManualHandler(new LoginController()));
        handlers.put("/login/view", new ManualHandler(new LoginViewController()));
        handlers.put("/logout", new ManualHandler(new LogoutController()));
        handlers.put("/register/view", new ManualHandler(new RegisterViewController()));
        handlers.put("/register", new ManualHandler(new RegisterController()));

        log.info("Initialized Handler Mapping!");
        handlers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, handlers.get(path).getClass()));
    }

    @Override
    public ManualHandler getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return handlers.get(requestURI);
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return handlers.containsKey(request.getRequestURI());
    }
}
