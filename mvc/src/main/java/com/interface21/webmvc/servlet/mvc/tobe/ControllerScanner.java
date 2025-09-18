package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public static Map<Class<?>, Object> createAllControllerInstances() {
        Reflections reflections = new Reflections("com.techcourse.controller");
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> controllerInstances = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object controllerInstance = controllerClass.getConstructor().newInstance();
                controllerInstances.put(controllerClass, controllerInstance);
            } catch (Exception exception) {
                log.error("알 수 없는 오류가 발생했습니다: " + exception.getMessage());
            }
        }
        return controllerInstances;
    }

}
