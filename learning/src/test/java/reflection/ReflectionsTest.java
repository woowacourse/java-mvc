package reflection;

import static org.assertj.core.api.Assertions.assertThat;

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

        Set<Class<?>> actual = reflections.getTypesAnnotatedWith(Controller.class);
        actual.addAll(reflections.getTypesAnnotatedWith(Service.class));
        actual.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        for (Class<?> clazz : actual) {
            log.info(clazz.getName());
        }

        assertThat(actual).hasSize(4);
    }
}
