package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        final Reflections reflections = new Reflections("reflection.examples");
        reflections.getConstructorsAnnotatedWith(Service.class);

        find(reflections,Controller.class);
        find(reflections,Service.class);
        find(reflections, Repository.class);
    }

    private void find(final Reflections reflections, final Class<? extends Annotation> annotation) {
        log(reflections.getTypesAnnotatedWith(annotation));
    }

    private void log(final Set<Class<?>> clazz) {
        log.info("{}", clazz);
    }
}
