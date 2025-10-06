package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.InitializableHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements InitializableHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, MethodHandler> methodHandlers;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.methodHandlers = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Set<Class<?>> controllerClasses = scanner.findControllerClasses();

        HandlerMappingFactory factory = new HandlerMappingFactory();
        methodHandlers.putAll(factory.createHandlerMappings(controllerClasses));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return methodHandlers.get(
                new HandlerKey(
                        request.getRequestURI(),
                        RequestMethod.valueOf(request.getMethod())
                )
        );
    }
}
