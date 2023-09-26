package webmvc.org.springframework.web.servlet.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.exception.NoSuchHandlerFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappings {

private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappings(final Object... basePackage) {
        this.annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(HttpServletRequest request) {
        Object annotationHandler = annotationHandlerMapping.getHandler(request);
        if (Objects.nonNull(annotationHandler)) {
            return annotationHandler;
        }
        throw new NoSuchHandlerFoundException();
    }
}
