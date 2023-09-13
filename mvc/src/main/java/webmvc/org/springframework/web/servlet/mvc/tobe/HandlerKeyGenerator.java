package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import web.org.springframework.web.bind.annotation.HttpMappings;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class HandlerKeyGenerator {

    private final HttpMappingExtractor httpMappingExtractor;

    public HandlerKeyGenerator(final HttpMappingExtractor httpMappingExtractor) {
        this.httpMappingExtractor = httpMappingExtractor;
    }

    public List<HandlerKey> generate(final String prefix, final Method method) {
        final Annotation annotation = getDeclaredAnnotations(method);
        final String uri = httpMappingExtractor.extractRequestUri(annotation);
        final RequestMethod[] requestMethods = httpMappingExtractor.extractRequestMethod(annotation);
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(prefix + uri, requestMethod))
                .collect(toList());
    }

    private Annotation getDeclaredAnnotations(final Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(HttpMappings::isAnyMatch)
                .findFirst()
                .orElseThrow();
    }
}
