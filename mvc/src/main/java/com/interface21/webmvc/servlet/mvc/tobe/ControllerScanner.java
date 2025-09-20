package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllerRegistry;

    public ControllerScanner(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        // @Controller 클래스 스캔
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        // (요청을 실제로 처리할) 인스턴스 매핑
        controllerRegistry = instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classSet){
        Map<Class<?>, Object> controllers = new HashMap<>();
        classSet.forEach(clazz -> {
            try {
                Object o = clazz.getDeclaredConstructor().newInstance();
                controllers.put(clazz, o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return controllers;
    }

    public Map<Class<?>, Object> getControllerRegistry(){
        return new HashMap<>(controllerRegistry);
    }
}
