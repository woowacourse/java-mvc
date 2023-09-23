package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.GetMapping;
import web.org.springframework.web.bind.annotation.PostMapping;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.exception.MappingAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnnotationHandlerKeyComposite {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerKeyComposite.class);
    private static final List<Class<? extends Annotation>> mappingAnnotations = new ArrayList<>();

    static {
        mappingAnnotations.add(RequestMapping.class);
        mappingAnnotations.add(GetMapping.class);
        mappingAnnotations.add(PostMapping.class);
    }

    public Optional<HandlerKey> getHandlerKey(final Method method) {
        return getMethodAnnotation(method)
                .map(methodAnnotation -> new HandlerKey(getRequestUrl(methodAnnotation), getRequestMethod(methodAnnotation)));
    }

    private Optional<Annotation> getMethodAnnotation(final Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(this::isExists)
                .findFirst();
    }

    private boolean isExists(final Annotation annotation) {
        final Class<? extends Annotation> annotationClazz = annotation.annotationType();

        return mappingAnnotations.stream()
                .anyMatch(mappingAnnotation -> mappingAnnotation.isAssignableFrom(annotationClazz));
    }

    private String getRequestUrl(final Annotation annotation) {
        try {
            final Method valueMethod = annotation.getClass().getDeclaredMethod("value");

            return (String) valueMethod.invoke(annotation);
        } catch (Exception e) {
            log.warn("전달 받은 Method에 해당하는 어노테이션으로 url 정보를 찾을 수 없습니다.", e);
            throw new MappingAnnotationException("[ERROR] 전달 받은 Method에 해당하는 어노테이션으로 url 정보를 찾을 수 없습니다.");
        }
    }

    private RequestMethod getRequestMethod(final Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            return ((RequestMapping) annotation).method();
        }

        return annotation.annotationType()
                .getDeclaredAnnotation(RequestMapping.class)
                .method();
    }
}
