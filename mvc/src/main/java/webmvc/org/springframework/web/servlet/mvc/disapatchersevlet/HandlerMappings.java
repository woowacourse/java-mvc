package webmvc.org.springframework.web.servlet.mvc.disapatchersevlet;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final Object[] basePackage;
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappings(final Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        final ControllerHandlerMapping controllerHandlerMapping = new ControllerHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
        controllerHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(controllerHandlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (Exception ignored) {
            }
        }
        return new ForwardController("/404.jsp");
    }
}
