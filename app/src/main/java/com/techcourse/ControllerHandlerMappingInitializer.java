package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;

public class ControllerHandlerMappingInitializer {

    public ControllerHandlerMapping handle() {
        final var handlerMapping = new ControllerHandlerMapping();

        handlerMapping.addController("/", new ForwardController("/index.jsp"));
        handlerMapping.addController("/login", new LoginController());
        handlerMapping.addController("/login/view", new LoginViewController());
        handlerMapping.addController("/logout", new LogoutController());
        handlerMapping.addController("/register/view", new RegisterViewController());
        handlerMapping.addController("/register", new RegisterController());

        return handlerMapping;
    }
}
