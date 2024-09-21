package reflection;

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

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            log.info(clazz.getSimpleName());
        }
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Service.class)) {
            log.info(clazz.getSimpleName());
        }
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Repository.class)) {
            log.info(clazz.getSimpleName());
        }
    }
}
