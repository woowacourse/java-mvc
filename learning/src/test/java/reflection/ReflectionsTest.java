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
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> typesAnnotatedWith1 = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> typesAnnotatedWith2 = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> clazz : typesAnnotatedWith) {
            log.debug("{} annotated class: {}", Controller.class.getSimpleName(), clazz.getName());
        }

        for (Class<?> clazz : typesAnnotatedWith1) {
            log.debug("{} annotated class: {}", Service.class.getSimpleName(), clazz.getName());
        }

        for (Class<?> clazz : typesAnnotatedWith2) {
            log.debug("{} annotated class: {}", Repository.class.getSimpleName(), clazz.getName());
        }
    }
}
