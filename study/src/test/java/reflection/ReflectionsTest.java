package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        System.out.println("===== @Controller =====");
        controllerClasses.forEach(clazz -> log.info(clazz.getName()));
        System.out.println("===== @Service =====");
        serviceClasses.forEach(clazz -> log.info(clazz.getName()));
        System.out.println("===== @Repository =====");
        repositoryClasses.forEach(clazz -> log.info(clazz.getName()));
    }
}
