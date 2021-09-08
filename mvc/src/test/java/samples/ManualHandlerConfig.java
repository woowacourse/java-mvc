package samples;

import java.util.HashMap;
import java.util.Map;

import air.annotation.Component;
import nextstep.configuration.HandlerConfigurer;
import nextstep.mvc.controller.asis.Controller;

@Component
public class ManualHandlerConfig implements HandlerConfigurer {

    @Override
    public Map<String, Controller> customHandlerSetting() {
        Map<String, Controller> handlers = new HashMap<>();
        handlers.put("/login", new TestInterfaceController());
        return handlers;
    }
}
