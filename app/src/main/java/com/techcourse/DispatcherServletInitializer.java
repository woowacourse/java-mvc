package com.techcourse;

import com.interface21.web.AbstractDispatcherServletInitializer;

public class DispatcherServletInitializer extends AbstractDispatcherServletInitializer {


    @Override
    protected String getBasePackage() {
        return "com.techcourse.controller";
    }
}
