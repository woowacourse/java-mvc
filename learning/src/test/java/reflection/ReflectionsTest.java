package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflector = new Reflections("examples");

        Set<Class<?>> controllers = reflector.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflector.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflector.getTypesAnnotatedWith(Repository.class);

        List<Set<Class<?>>> foundClasses = Arrays.asList(controllers, services, repositories);

        for (Set<Class<?>> annotatedClasses : foundClasses) {
            annotatedClasses
                .forEach(it -> System.out.println("type : " + Arrays.stream(it.getAnnotations())
                    .map(annotation -> annotation.annotationType().getSimpleName())
                    .findFirst()
                    .orElse("Nothing Matching")
                    + " || class Name : " + it.getSimpleName()));
        }

    }
}
