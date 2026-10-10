package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;

public class ControllerScanner {

    private ControllerScanner() {
    }

    public static List<Object> scan(Object pkg) {
        final var reflections = new Reflections(pkg.toString());
        var controllers = reflections.getTypesAnnotatedWith(Controller.class);

        List<Object> instances = new ArrayList<>();
        for (Class<?> clazz : controllers) {
            try {
                Object controller = clazz.getDeclaredConstructor().newInstance();
                instances.add(controller);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }

        return instances;
    }
}
