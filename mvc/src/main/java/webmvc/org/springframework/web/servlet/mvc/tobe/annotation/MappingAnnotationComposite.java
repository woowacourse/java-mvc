package webmvc.org.springframework.web.servlet.mvc.tobe.annotation;

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

public class MappingAnnotationComposite {

    private static final Logger log = LoggerFactory.getLogger(MappingAnnotationComposite.class);
    private static final List<Class<? extends Annotation>> mappingAnnotations = new ArrayList<>();

    static {
        mappingAnnotations.add(RequestMapping.class);
        mappingAnnotations.add(GetMapping.class);
        mappingAnnotations.add(PostMapping.class);
    }

    public  String getRequestUrl(final Method method) {
        final Annotation annotation = getMethodAnnotation(method);

        try {
            final Method valueMethod = annotation.getClass().getDeclaredMethod("value");

            return (String) valueMethod.invoke(annotation);
        } catch (Exception e) {
            log.warn("전달 받은 Method에 해당하는 어노테이션으로 url 정보를 찾을 수 없습니다.", e);
            throw new MappingAnnotationException("[ERROR] 전달 받은 Method에 해당하는 어노테이션으로 url 정보를 찾을 수 없습니다.");
        }
    }

    private Annotation getMethodAnnotation(final Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(this::isExists)
                .findFirst()
                .orElseThrow();
    }

    private boolean isExists(final Annotation annotation) {
        final Class<? extends Annotation> annotationType = annotation.annotationType();
        return mappingAnnotations.stream()
                .anyMatch(mappingAnnotation -> mappingAnnotation.isAnnotationPresent(annotationType));
    }

    public RequestMethod getRequestMethod(final Method method) {
        final Annotation annotation = getMethodAnnotation(method);
        if (annotation instanceof RequestMapping) {
            return ((RequestMapping) annotation).method();
        }

        return annotation.getClass()
                .getDeclaredAnnotation(RequestMapping.class)
                .method();
    }
}
