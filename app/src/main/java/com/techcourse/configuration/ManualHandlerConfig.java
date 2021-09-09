package com.techcourse.configuration;

import java.util.HashMap;
import java.util.Map;

import com.techcourse.controller.*;

import air.annotation.Configuration;
import nextstep.configuration.HandlerConfigurer;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;

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
