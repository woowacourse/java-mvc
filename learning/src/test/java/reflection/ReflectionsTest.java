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

        Set<Class<?>> classesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> classesAnnotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> classesAnnotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);

        loggingClasses(Controller.class, classesAnnotatedWithController);
        loggingClasses(Service.class, classesAnnotatedWithService);
        loggingClasses(Repository.class, classesAnnotatedWithRepository);
    }

    private void loggingClasses(Class<?> annotationClass, Set<Class<?>> annotatedClass) {
        for (Class<?> clazz : annotatedClass) {
            log.debug("@{} annotated class: {}", annotationClass.getSimpleName(), clazz.getName());
        }
    }
}
