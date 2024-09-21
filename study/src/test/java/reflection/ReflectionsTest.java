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

        Set<Class<?>> controller = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> service = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repository = reflections.getTypesAnnotatedWith(Repository.class);

        controller.forEach(clazz -> log.info(clazz.getName()));
        service.forEach(clazz -> log.info(clazz.getName()));
        repository.forEach(clazz -> log.info(clazz.getName()));
    }
}
