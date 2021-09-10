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
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        controllers.forEach(con -> log.info(con.getName()));
        services.forEach(ser -> log.info(ser.getName()));
        repositories.forEach(rep -> log.info(rep.getName()));
    }
}
