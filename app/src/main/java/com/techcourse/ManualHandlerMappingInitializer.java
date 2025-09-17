package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;

public class ManualHandlerMappingInitializer {

    public ManualHandlerMapping initialize() {
        final var mapping = new ManualHandlerMapping();

        mapping.register("/", new ForwardController("/index.jsp"));
        mapping.register("/login", new LoginController());
        mapping.register("/login/view", new LoginViewController());
        mapping.register("/logout", new LogoutController());
        mapping.register("/register/view", new RegisterViewController());
        mapping.register("/register", new RegisterController());

        return mapping;
    }
}
