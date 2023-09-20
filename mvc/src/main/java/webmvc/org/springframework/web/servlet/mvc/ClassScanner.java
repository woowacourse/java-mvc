package webmvc.org.springframework.web.servlet.mvc;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.reflections.Reflections;

public class ClassScanner {

    private final Reflections reflections;

    public ClassScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> findClassesHasAnnotation(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
