package com.techcourse.air.mvc.configuration;

import java.util.HashMap;
import java.util.Map;

import com.techcourse.air.mvc.core.controller.asis.Controller;

public interface HandlerConfigurer {
    default Map<String, Controller> customHandlerSetting() {
        return new HashMap<>();
    }
}
