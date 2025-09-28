package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.dispatcherservlet.AppConfig;
import java.util.HashMap;
import java.util.Map;

public class TechcourseAppConfig implements AppConfig {

    @Override
    public Map<String, Controller> getManualControllers() {
        final Map<String, Controller> controllers = new HashMap<>();
        return controllers;
    }

    @Override
    public String getControllerBasePackage() {
        return "com.techcourse.controller";
    }
}
