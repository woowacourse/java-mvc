package com.techcourse.air.configuration;

import java.util.HashMap;
import java.util.Map;

import com.techcourse.air.controller.LogoutController;
import com.techcourse.air.controller.RegisterController;
import com.techcourse.air.controller.RegisterViewController;

import com.techcourse.air.core.annotation.Configuration;
import com.techcourse.air.mvc.configuration.HandlerConfigurer;
import com.techcourse.air.mvc.core.controller.asis.Controller;
import com.techcourse.air.mvc.core.controller.asis.ForwardController;

@Configuration
public class ManualHandlerConfig implements HandlerConfigurer {

    @Override
    public Map<String, Controller> customHandlerSetting() {
        Map<String, Controller> controllers = new HashMap<>();
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());
        return controllers;
    }
}
