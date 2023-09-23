package com.techcourse;

import com.techcourse.controllerv1.LoginControllerV1;
import com.techcourse.controllerv1.LoginViewControllerV1;
import com.techcourse.controllerv1.LogoutControllerV1;
import com.techcourse.controllerv1.RegisterControllerV1;
import com.techcourse.controllerv1.RegisterViewControllerV1;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;
import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<HandlerKey, Controller> controllers = new HashMap<>();

    public ManualHandlerMapping() {
        initialize();
    }

    @Override
    public void initialize() {
        controllers.put(new HandlerKey("/", GET), new ForwardController("/index.jsp"));
        controllers.put(new HandlerKey("/v1/login", POST), new LoginControllerV1());
        controllers.put(new HandlerKey("/v1/login/view", GET), new LoginViewControllerV1());
        controllers.put(new HandlerKey("/v1/logout", GET), new LogoutControllerV1());
        controllers.put(new HandlerKey("/v1/register/view", GET), new RegisterViewControllerV1());
        controllers.put(new HandlerKey("/v1/register", POST), new RegisterControllerV1());

        log.info("Initialized Manual Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        return controllers.containsKey(getHandlerKey(request));
    }

    @Override
    public Object getHandlerExecution(final HttpServletRequest request) {
        final HandlerKey handlerKey = getHandlerKey(request);

        return controllers.get(handlerKey);
    }

    @Override
    public List<HandlerKey> getHandlerKeys() {
        return new ArrayList<>(controllers.keySet());
    }
}
