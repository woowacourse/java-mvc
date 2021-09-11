package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.lang.annotation.Annotation;
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
        Reflections reflections = new Reflections("examples");

        final List<Class<? extends Annotation>> classes = Arrays.asList(Controller.class, Service.class, Repository.class);

        for (Class<? extends Annotation> clazz : classes) {
            final Set<Class<?>> types = reflections.getTypesAnnotatedWith(clazz);
            for (Class<?> type : types) {
                log.debug(type.getSimpleName());
            }
        }
    }
}
