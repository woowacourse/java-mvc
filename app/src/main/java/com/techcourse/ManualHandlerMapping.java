package com.techcourse;

import com.techcourse.controller.manual.ManualLoginController;
import com.techcourse.controller.manual.ManualLoginViewController;
import com.techcourse.controller.manual.ManualLogoutController;
import com.techcourse.controller.manual.ManualRegisterController;
import com.techcourse.controller.manual.ManualRegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ManualHomeController;
import nextstep.mvc.handler.mapping.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(ManualHandlerMapping.class);
    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/manual", new ManualHomeController("/index.jsp"));
        controllers.put("/manual/login", new ManualLoginController());
        controllers.put("/manual/login/view", new ManualLoginViewController());
        controllers.put("/manual/logout", new ManualLogoutController());
        controllers.put("/manual/register/view", new ManualRegisterViewController());
        controllers.put("/manual/register", new ManualRegisterController());

        LOG.info("Initialize ManualHandlerMapping!");

        for (String path : controllers.keySet()) {
            LOG.info("Path: {}, Controller: {}", path, controllers.get(path).getClass());
        }
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        LOG.debug("Request Mapping URI : {}", requestURI);

        return controllers.get(requestURI);
    }
}
