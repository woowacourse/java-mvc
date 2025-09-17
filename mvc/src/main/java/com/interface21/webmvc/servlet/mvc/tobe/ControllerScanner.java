package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public static Map<Class<?>, Object> scanControllers(Object[] basePackage) throws Exception {
        Map<Class<?>, Object> controllerInfos = new HashMap<>();
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            Object object = controller.getDeclaredConstructor().newInstance();
            controllerInfos.put(controller, object);
        }
        return controllerInfos;
    }
}
