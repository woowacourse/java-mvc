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
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> repositoryClass = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);

        for (Class<?> clazz : controllerClass) {
            log.debug("controllerClass : " + clazz.getName());
        }
        for (Class<?> clazz : repositoryClass) {
            log.debug("repositoryClass : " + clazz.getName());
        }
        for (Class<?> clazz : serviceClass) {
            log.debug("serviceClass : " + clazz.getName());
        }
    }
}
