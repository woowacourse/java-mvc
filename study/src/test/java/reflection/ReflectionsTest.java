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

        final Set<Class<?>> clazzWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("=================> Controllers = {}", clazzWithControllerAnnotation);

        final Set<Class<?>> clazzWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        log.info("=================> Services = {}", clazzWithServiceAnnotation);

        final Set<Class<?>> clazzWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("=================> Repositories = {}", clazzWithRepositoryAnnotation);
    }
}
