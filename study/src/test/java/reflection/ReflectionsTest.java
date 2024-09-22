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
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(Service.class));
        typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        typesAnnotatedWith.forEach(type -> log.info("{}", type.getName()));
    }
}
