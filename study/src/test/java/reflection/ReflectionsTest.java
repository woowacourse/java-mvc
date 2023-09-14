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

        printClass(reflections.getTypesAnnotatedWith(Controller.class));
        printClass(reflections.getTypesAnnotatedWith(Service.class));
        printClass(reflections.getTypesAnnotatedWith(Repository.class));
    }

    private void printClass(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            log.info("{}", clazz);
        }
    }
}
