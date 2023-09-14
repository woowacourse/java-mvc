package reflection;

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

        findAnnotations(reflections, Controller.class);
        findAnnotations(reflections, Service.class);
        findAnnotations(reflections, Repository.class);
    }

    private void findAnnotations(final Reflections reflections, final Class annotation) {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(annotation);
        for (Class<?> controller : controllers) {
            log.info("find {} = {}", annotation.getSimpleName(), controller.getSimpleName());
        }
    }
}
