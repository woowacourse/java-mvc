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

        Set<Class<?>> classesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> classesWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> classesWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);

        classesWithControllerAnnotation.forEach(c -> log.info("Controller class = {}", c.getSimpleName()));
        classesWithServiceAnnotation.forEach(c -> log.info("service class = {}", c.getSimpleName()));
        classesWithRepositoryAnnotation.forEach(c -> log.info("repository class = {}", c.getSimpleName()));
    }
}
