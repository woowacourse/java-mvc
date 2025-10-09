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
        final String lookupPath = extractLookupPath(request);
        return methodHandlers.get(new HandlerKey(lookupPath, RequestMethod.valueOf(request.getMethod())));
    }

    private String extractLookupPath(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final String contextPath = request.getContextPath();

        if (contextPath == null || contextPath.isEmpty()) {
            return requestUri;
        }

        if (!requestUri.startsWith(contextPath)) {
            return requestUri;
        }

        return requestUri.substring(contextPath.length());
    }
}
