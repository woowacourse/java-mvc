package com.techcourse;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.LegacyRequestHandlerImpl;
import com.interface21.webmvc.servlet.RequestHandler;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<HandlerKey, RequestHandler> controllers = new HashMap<>();

    @Override
    public void initialize() {
        log.info("Initializing Handler Mapping!");
        registerLegacyControllers();
    }

    private void registerLegacyControllers() {
        controllers.put(new HandlerKey("/", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new ForwardController("/index.jsp")));
        controllers.put(new HandlerKey("/login", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new LoginController()));
        controllers.put(new HandlerKey("/login/view", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new LoginViewController()));
        controllers.put(new HandlerKey("/logout", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new LogoutController()));
    }

    @Override
    public RequestHandler getHandler(final String requestMethod, final String requestURI) {
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(new HandlerKey(requestURI, RequestMethod.of(requestMethod)));
    }
}
