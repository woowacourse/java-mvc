package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class HandlerKeyGenerator {

    private final HttpMappingAnnotationParser httpMappingAnnotationParser;

    public HandlerKeyGenerator() {
        this(new HttpMappingAnnotationParser());
    }

    public HandlerKeyGenerator(final HttpMappingAnnotationParser httpMappingAnnotationParser) {
        this.httpMappingAnnotationParser = httpMappingAnnotationParser;
    }

    public List<HandlerKey> generate(final String prefix, final Method method) {
        final Annotation annotation = httpMappingAnnotationParser.parseAnnotation(method);
        final String uri = httpMappingAnnotationParser.parseRequestUri(annotation);
        final RequestMethod[] requestMethods = httpMappingAnnotationParser.parseRequestMethod(annotation);
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(prefix + uri, requestMethod))
                .collect(toList());
    }
}
