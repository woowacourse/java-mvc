package reflection;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        logAnnotation(reflections, Controller.class);
        logAnnotation(reflections, Service.class);
        logAnnotation(reflections, Repository.class);
    }

    private void logAnnotation(final Reflections reflections, final Class<? extends Annotation> annotation) {
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
        typesAnnotatedWith.stream()
                .map(Class::getSimpleName)
                .forEach(name -> log.info(name));
    }
}
