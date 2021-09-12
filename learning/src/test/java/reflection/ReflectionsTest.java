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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClass = reflections.getTypesAnnotatedWith(Repository.class);

        controllerClass
            .forEach(controller -> log.info("Class With @Controller : {}", controller.getName()));

        serviceClass
            .forEach(service -> log.info("Class With @Service : {}", service.getName()));

        repositoryClass
            .forEach(repository -> log.info("Class With @Repository : {}", repository.getName()));
    }
}
