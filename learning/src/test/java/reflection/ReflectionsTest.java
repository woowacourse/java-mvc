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
        // given
        Reflections reflections = new Reflections("examples");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        // when, then
        for (Class<?> controller : controllers) {
            log.info("name : {}", controller.getSimpleName());
        }
        for (Class<?> service : services) {
            log.info("name : {}", service.getSimpleName());
        }
        for (Class<?> repository : repositories) {
            log.info("name : {}", repository.getSimpleName());
        }
    }
}
