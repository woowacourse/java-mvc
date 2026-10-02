package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> annotatedTypes = new HashSet<>();
        annotatedTypes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        annotatedTypes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        annotatedTypes.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        annotatedTypes.stream()
                .sorted(Comparator.comparing(Class::getName))
                .forEach(type -> log.info("{}", type.getName()));
    }
}
