package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;

public class ControllerRegistry {

    private static final Map<Class<?>, Object> controllers = new HashMap<>();

    public static Object getOrCreateController(Class<?> controller) throws Exception {
        if (controllers.containsKey(controller)) {
            return controllers.get(controller);
        }

        Object controllerInstance = controller.getConstructor().newInstance();
        controllers.put(controller, controllerInstance);
        return controllerInstance;
    }
}
