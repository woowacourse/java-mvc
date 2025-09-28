package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.dispatcherservlet.AppConfig;

public class TechcourseAppConfig implements AppConfig {

    @Override
    public String getControllerBasePackage() {
        return "com.techcourse.controller";
    }
}
