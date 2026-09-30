package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

//TODO 컴포넌트 스캐너로 변경
public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(Object... basePackages) {
        this.basePackages = basePackages;
    }

    public List<Class<?>> scan() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        return List.copyOf(controllerTypes);
    }
}
