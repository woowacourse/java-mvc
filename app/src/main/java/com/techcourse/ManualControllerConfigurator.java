package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import java.util.HashMap;
import java.util.Map;

public class ManualControllerConfigurator {

    public static Map<String, Controller> getManualControllers() {
        final Map<String, Controller> controllers = new HashMap<>();
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());
        return controllers;
    }
}
