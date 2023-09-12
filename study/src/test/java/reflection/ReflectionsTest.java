package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controller = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("@Controller");
        for (Class<?> clazz : controller) {
            log.info(clazz.getName());
        }

        Set<Class<?>> service = reflections.getTypesAnnotatedWith(Service.class);
        log.info("@Service");
        for (Class<?> clazz : service) {
            log.info(clazz.getName());
        }

        Set<Class<?>> repository = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("@Repository");
        for (Class<?> clazz : repository) {
            log.info(clazz.getName());
        }
    }
}
