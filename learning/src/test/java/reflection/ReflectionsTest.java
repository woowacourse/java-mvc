package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");
        Set<Class<?>> classesWithController = reflections.getTypesAnnotatedWith(Controller.class);
        classesWithController.forEach(
            c -> log.info(">>> @Controller at {}", c.getSimpleName())
        );

        Set<Class<?>> classesWithService = reflections.getTypesAnnotatedWith(Service.class);
        classesWithService.forEach(
            c -> log.info(">>> @Service at {}", c.getSimpleName())
        );

        Set<Class<?>> classesWithRepository = reflections.getTypesAnnotatedWith(Repository.class);
        classesWithRepository.forEach(
            c -> log.info(">>> @Repository at {}", c.getSimpleName())
        );

    }
}
