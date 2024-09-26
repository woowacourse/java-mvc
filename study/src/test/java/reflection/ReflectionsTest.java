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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(clazz -> log.info(clazz.getName()));
        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(clazz -> log.info(clazz.getName()));
        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(clazz -> log.info(clazz.getName()));
    }
}
