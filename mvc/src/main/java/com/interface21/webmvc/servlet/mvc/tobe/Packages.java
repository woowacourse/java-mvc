package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packages {

    private static final Logger log = LoggerFactory.getLogger(Packages.class);

    private final Object[] basePackages;
    private final Map<Object, List<Method>> controllerMap;

    public Packages(Object[] basePackages) {
        validate(basePackages);
        this.basePackages = basePackages;
        this.controllerMap = new HashMap<>();
    }

    private void validate(Object[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            throw new IllegalArgumentException("basePackages는 비어있을 수 없습니다.");
        }
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            controllers.forEach(controller -> {
                Object controllerInstance = createControllerInstance(controller);

                if (controllerInstance != null) {
                    List<Method> requestMappingMethods = getRequestMappingMethods(controller);
                    controllerMap.put(controllerInstance, requestMappingMethods);
                }
            });
        }
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("생성자를 호출할 수 없습니다. " + controllerClass.getName());
        } catch (InstantiationException e) {
            log.error("인스턴스화 할 수 없습니다. " + controllerClass.getName());
        }
        return null;
    }

    public List<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(this::isRequestMappingAnnotated)
                .toList();
    }

    private boolean isRequestMappingAnnotated(Method method) {
        return method.getAnnotation(RequestMapping.class) != null;
    }

    public List<Object> controllerInstances() {
        return controllerMap.keySet().stream().toList();
    }

    public List<Method> findRequestMappingMethodsByControllerInstance(Object controllerInstance) {
        return controllerMap.get(controllerInstance);
    }
}
