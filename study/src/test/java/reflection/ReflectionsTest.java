package reflection;

import java.lang.annotation.Annotation;
import java.util.List;
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
        final Reflections reflections = new Reflections("reflection.examples");
        final List<Class<? extends Annotation>> annotations = List.of(
                Controller.class,
                Service.class,
                Repository.class
        );

        for (final Class<? extends Annotation> annotation : annotations) {
            reflections.getTypesAnnotatedWith(annotation)
                    .forEach(it -> log.info(it.getSimpleName()));
        }
    }
}
