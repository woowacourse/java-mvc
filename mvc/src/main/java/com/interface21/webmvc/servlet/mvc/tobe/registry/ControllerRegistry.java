package com.interface21.webmvc.servlet.mvc.tobe.registry;

import com.interface21.webmvc.servlet.mvc.tobe.InstanceMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ControllerRegistry {

    private static final Logger log = LoggerFactory.getLogger(ControllerRegistry.class);
    private static final Map<Class<?>, Object> controllers = new HashMap<>();

    private ControllerRegistry() {
    }

    public static Object getOrCreateController(Class<?> controller) {
        if (controllers.containsKey(controller)) {
            return controllers.get(controller);
        }
        Object controllerInstance = InstanceMaker.makeInstance(controller);
        controllers.put(controller, controllerInstance);

        log.info("Controller Registered Success");
        return controllerInstance;
    }
}
