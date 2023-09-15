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
        // main 패키지 > reflection > examples 에 있는 Reflection들을 모두 가져온다.

        final Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(
            Controller.class);
        final Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositoryClass = reflections.getTypesAnnotatedWith(
            Repository.class);

        log.info("{}", controllerClass);
        log.info("{}", serviceClass);
        log.info("{}", repositoryClass);
    }
}
