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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        final Set<Class<?>> controllerClass = reflections
                .getTypesAnnotatedWith(Controller.class);
        log.debug("@Controller: {}", controllerClass.toString());

        final Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);
        log.debug("@Service: {}", serviceClass.toString());

        final Set<Class<?>> repositoryClass = reflections
                .getTypesAnnotatedWith(Repository.class);
        log.debug("@Repository: {}", repositoryClass.toString());
    }
}
