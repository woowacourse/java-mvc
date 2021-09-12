package com.techcourse;

import com.techcourse.controller.manual.LoginController;
import com.techcourse.controller.manual.LoginViewController;
import com.techcourse.controller.manual.LogoutController;
import com.techcourse.controller.manual.RegisterController;
import com.techcourse.controller.manual.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.handler.mapping.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(ManualHandlerMapping.class);
    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/manual/login", new LoginController());
        controllers.put("/manual/login/view", new LoginViewController());
        controllers.put("/manual/logout", new LogoutController());
        controllers.put("/manual/register/view", new RegisterViewController());
        controllers.put("/manual/register", new RegisterController());

        LOG.info("Initialize ManualHandlerMapping!");

        for (String path : controllers.keySet()) {
            LOG.info("Path: {}, Controller: {}", path, controllers.get(path).getClass());
        }
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        LOG.debug("Request Mapping Uri : {}", requestURI);

        return controllers.get(requestURI);
    }
}
