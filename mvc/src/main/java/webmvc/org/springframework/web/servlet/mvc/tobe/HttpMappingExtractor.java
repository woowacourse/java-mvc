package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class HttpMappingExtractor {

    private static final String URI_FIELD_NAME = "value";

    public String extractRequestUri(final Annotation annotation) {
        final Class<?> annotationType = annotation.annotationType();
        try {
            final Method method = annotationType.getDeclaredMethod(URI_FIELD_NAME);
            return (String) method.invoke(annotation);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Invoke Failed!");
        }
    }

    public RequestMethod[] extractRequestMethod(final Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            return ((RequestMapping) annotation).method();
        }
        return annotation.annotationType()
                .getDeclaredAnnotation(RequestMapping.class)
                .method();
    }
}
