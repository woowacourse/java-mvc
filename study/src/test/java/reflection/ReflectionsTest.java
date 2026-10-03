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

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> aClass : typesAnnotatedWith) {
            log.info(aClass.getName());
        }

        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> aClass : services) {
            log.info(aClass.getName());
        }

        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> aClass : repositories) {
            log.info(aClass.getName());
        }
    }
}
