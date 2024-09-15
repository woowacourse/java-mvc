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

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        logClassesWithAnnotation(controllerClasses, "@Controller");

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        logClassesWithAnnotation(serviceClasses, "@Service");

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        logClassesWithAnnotation(repositoryClasses, "@Repository");
    }

    private void logClassesWithAnnotation(Set<Class<?>> classes, String annotationName) {
        if (classes.isEmpty()) {
            log.info("No classes found with annotation {}", annotationName);
        } else {
            for (Class<?> clazz : classes) {
                log.info("Class {} is annotated with {}", clazz.getName(), annotationName);
            }
        }
    }
}
