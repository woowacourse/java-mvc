package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> controller: controllers) {
            log.info("컨트롤러: {}", controller.getName());
        }

        for (Class<?> service: services) {
            log.info("서비스: {}", service.getName());
        }

        for (Class<?> repository: repositories) {
            log.info("레파지토리: {}", repository.getName());
        }
    }
}
