package samples;

import java.util.HashMap;
import java.util.Map;

import com.techcourse.air.core.annotation.Component;
import com.techcourse.air.mvc.configuration.HandlerConfigurer;
import com.techcourse.air.mvc.core.controller.asis.Controller;

@Component
public class ManualHandlerConfig implements HandlerConfigurer {

    @Override
    public Map<String, Controller> customHandlerSetting() {
        Map<String, Controller> handlers = new HashMap<>();
        handlers.put("/login", new TestInterfaceController());
        return handlers;
    }
}
