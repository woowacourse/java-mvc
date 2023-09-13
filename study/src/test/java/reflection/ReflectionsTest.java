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

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        log.info(controllers.toString());

        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        log.info(services.toString());

        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        log.info(repositories.toString());
    }
}
