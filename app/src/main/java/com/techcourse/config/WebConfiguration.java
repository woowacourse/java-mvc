package com.techcourse.config;

import com.techcourse.controller.HomeController;
import nextstep.core.annotation.Configuration;
import nextstep.mvc.WebMvcConfigurer;
import nextstep.mvc.controller.ControllerContainer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addController(ControllerContainer controllerContainer) {
        controllerContainer.addController("/", new HomeController());
    }
}
