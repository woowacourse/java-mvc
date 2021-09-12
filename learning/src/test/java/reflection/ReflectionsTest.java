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

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(controller -> LOG.info(String.format("%s : %s", controller.getName(), controller.getAnnotation(Controller.class))));

        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        services.forEach(service -> LOG.info(String.format("%s : %s", service.getName(), service.getAnnotation(Service.class))));

        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        repositories.forEach(repository -> LOG.info(String.format("%s : %s", repository.getName(), repository.getAnnotation(Repository.class))));
    }
}
