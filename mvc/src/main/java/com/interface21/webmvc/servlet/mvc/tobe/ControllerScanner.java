package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        final Map<Class<?>, Object> controllersMap = new HashMap<>();
        for (Class<?> controllerType : controllers) {
            try {
                Constructor<?> constructor = controllerType.getConstructor();
                Object controller = constructor.newInstance();
                controllersMap.put(controllerType, controller);
            }  catch (Exception e) {
                throw new IllegalStateException("컨트롤러 인스턴스 생성 실패: " + controllerType, e);
            }
        }
        return controllersMap;
    }

}
