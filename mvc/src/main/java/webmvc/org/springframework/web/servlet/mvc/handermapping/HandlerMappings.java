package webmvc.org.springframework.web.servlet.mvc.handermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HandlerMappings {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappings(final String packageName) {
        this.annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
    }

    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(final HttpServletRequest request) {
        final Object annotationHandler = annotationHandlerMapping.getHandler(request);
        if (Objects.nonNull(annotationHandler)) {
            return annotationHandler;
        }

        throw new NoSuchElementException("핸들러를 찾을 수 없습니다.");
    }
}
