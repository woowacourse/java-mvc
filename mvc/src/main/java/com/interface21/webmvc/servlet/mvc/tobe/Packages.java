package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Packages {

    private final Object[] basePackages;
    private final Map<Object, List<Method>> controllerMap;

    public Packages(Object[] basePackages) {
        this.basePackages = basePackages;
        this.controllerMap = new HashMap<>();
    }

    public void initialize() {
        Map<Object, List<Method>> scanned = ControllerScanner.scan(basePackages);
        controllerMap.putAll(scanned);
    }

    public List<Object> controllerInstances() {
        return controllerMap.keySet().stream().toList();
    }

    public List<Method> findRequestMappingMethodsByControllerInstance(Object controllerInstance) {
        return controllerMap.get(controllerInstance);
    }
}
