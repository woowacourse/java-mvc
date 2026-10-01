package reflection;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Consumer;
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

        Consumer<Class<?>> printLog = typesAnnotatedWith -> log.info(typesAnnotatedWith.getName());
        log.info("Controller: ");
        reflections.getTypesAnnotatedWith(Controller.class).forEach(printLog);
        log.info("Service: ");
        reflections.getTypesAnnotatedWith(Service.class).forEach(printLog);
        log.info("Repository: ");
        reflections.getTypesAnnotatedWith(Repository.class).forEach(printLog);
    }
}
