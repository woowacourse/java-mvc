package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        System.out.println("********* CONTROLLER ***********");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(controller -> log.info(String.format("%s : %s", controller.getName(), controller.getAnnotation(Controller.class))));

        System.out.println("********* SERVICE ***********");
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        services.forEach(service -> log.info(String.format("%s : %s", service.getName(), service.getAnnotation(Service.class))));

        System.out.println("********* REPOSITORY ***********");
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        repositories.forEach(repository -> log.info(String.format("%s : %s", repository.getName(), repository.getAnnotation(Repository.class))));
    }
}
