package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Packages {

    private final Object[] basePackages;
    private final Map<Object, List<Method>> controllerMap;

    public Packages(Object[] basePackages) {
        validatePackagesEmpty(basePackages);
        this.basePackages = basePackages;
        this.controllerMap = new HashMap<>();
    }

    private void validatePackagesEmpty(Object[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            throw new IllegalArgumentException("basePackages는 비어있을 수 없습니다.");
        }
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            Map<Object, List<Method>> scanned = ControllerScanner.scan(basePackage);
            controllerMap.putAll(scanned);
        }
    }

    public List<Object> controllerInstances() {
        return controllerMap.keySet().stream().toList();
    }

    public List<Method> findRequestMappingMethodsByControllerInstance(Object controllerInstance) {
        return controllerMap.get(controllerInstance);
    }
}
