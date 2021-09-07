package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(findClassesByAnnotation(Controller.class));
        classes.addAll(findClassesByAnnotation(Service.class));
        classes.addAll(findClassesByAnnotation(Repository.class));
        classes.forEach(clazz -> log.info(clazz.getName()));
    }

    private Set<Class<?>> findClassesByAnnotation(Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections("examples");
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
