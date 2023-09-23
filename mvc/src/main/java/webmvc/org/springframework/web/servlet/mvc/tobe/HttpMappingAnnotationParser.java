package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import web.org.springframework.web.bind.annotation.HttpMappings;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class HttpMappingAnnotationParser {

    private static final String URI_FIELD_NAME = "value";

    public Annotation parseAnnotation(final Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(HttpMappings::isAnyMatch)
                .findFirst()
                .orElseThrow();
    }

    public String parseRequestUri(final Annotation annotation) {
        final Class<?> annotationType = annotation.annotationType();
        try {
            final Method method = annotationType.getDeclaredMethod(URI_FIELD_NAME);
            return (String) method.invoke(annotation);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Invoke Failed!");
        }
    }

    public RequestMethod[] parseRequestMethod(final Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            return ((RequestMapping) annotation).method();
        }
        return annotation.annotationType()
                .getDeclaredAnnotation(RequestMapping.class)
                .method();
    }
}
