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
        log.info("Controller 클래스: {}", controllerClasses);

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        log.info("Service 클래스: {}", serviceClasses);

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("Repository 클래스: {}", repositoryClasses);
    }
}
