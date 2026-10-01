package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);
    List<Class<? extends Annotation>> annotationTypes = List.of(
            Controller.class,
            Service.class,
            Repository.class
    );

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");

        for (Class<? extends Annotation> annotationType : annotationTypes) {
            Set<Class<?>> classes =
                    reflections.getTypesAnnotatedWith(annotationType);

            for (Class<?> clazz : classes) {
                log.info("Annotation: {}, class: {}",
                        annotationType.getSimpleName(),
                        clazz.getName());
            }
        }
    }
}
