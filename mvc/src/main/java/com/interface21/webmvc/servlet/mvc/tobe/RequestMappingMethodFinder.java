package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.reflections.ReflectionUtils;

import com.interface21.web.bind.annotation.RequestMapping;

public class RequestMappingMethodFinder {

    public Set<Method> getRequestMappingMethods(Set<Class<?>> controllerClasses) {
        Set<Method> requestMappingMethods = new HashSet<>();
        for (Class<?> controllerClass : controllerClasses) {
            requestMappingMethods.addAll(ReflectionUtils.getAllMethods(controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return requestMappingMethods;
    }
}

