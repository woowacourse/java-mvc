package reflection;

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

        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("annotated with @Controller : {}", controllers);
        final var services = reflections.getTypesAnnotatedWith(Service.class);
        log.info("annotated with @Service : {}", services);
        final var repositories = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("annotated with @Repository : {}", repositories);

    }
}
