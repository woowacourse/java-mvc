package reflection;

import java.lang.annotation.Annotation;

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
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");

        logAnnotatedClass(reflections, Controller.class);
        logAnnotatedClass(reflections, Service.class);
        logAnnotatedClass(reflections, Repository.class);
    }

    private void logAnnotatedClass(final Reflections reflections, final Class<? extends Annotation> annotationType) {
        reflections.getTypesAnnotatedWith(annotationType)
                .stream()
                .map(this::getClassName)
                .forEach(log::info);
    }

    private String getClassName(final Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
