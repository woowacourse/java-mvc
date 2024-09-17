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

        logAnnotationClass(reflections, Controller.class);
        logAnnotationClass(reflections, Service.class);
        logAnnotationClass(reflections, Repository.class);
    }

    void logAnnotationClass(Reflections reflections, Class<? extends Annotation> clazzType) {
        reflections.getTypesAnnotatedWith(clazzType)
                .forEach(clazz -> log.info("{}: {}", clazzType.getSimpleName(), clazz.getName()));
    }
}
