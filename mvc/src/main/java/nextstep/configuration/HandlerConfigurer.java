package nextstep.configuration;

import java.util.HashMap;
import java.util.Map;

import nextstep.mvc.controller.asis.Controller;

public interface HandlerConfigurer {
    default Map<String, Controller> customHandlerSetting() {
        return new HashMap<>();
    }
}
