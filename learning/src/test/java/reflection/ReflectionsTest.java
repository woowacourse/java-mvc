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

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> controllerClass : controllerClasses) {
            LOG.debug(controllerClass.getName());
        }

        for (Class<?> serviceClass : serviceClasses) {
            LOG.debug(serviceClass.getName());
        }

        for (Class<?> repositoryClass : repositoryClasses) {
            LOG.debug(repositoryClass.getName());
        }
    }
}
