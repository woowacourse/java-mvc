package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(a -> log.debug("Controller Bean: {}", a.getSimpleName()));

        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(a -> log.debug("Service Bean: {}", a.getSimpleName()));

        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(a -> log.debug("Repository Bean: {}", a.getSimpleName()));
    }
}
