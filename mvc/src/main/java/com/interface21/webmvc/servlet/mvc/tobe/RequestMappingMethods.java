package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMappingMethods {

    private final Set<Method> methods;

    public RequestMappingMethods(Controllers controllers) {
        methods = controllers.getControllerClasses().stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    public Set<Method> getMethods() {
        return methods;
    }
}
