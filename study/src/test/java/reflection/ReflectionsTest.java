package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        List<Class<? extends Annotation>> annotations = List.of(Controller.class, Service.class, Repository.class);

        for (Class<? extends Annotation> annotation : annotations) {
            for (Class<?> clazz : reflections.getTypesAnnotatedWith(annotation)) {
                log.info("@{} : {}", annotation.getSimpleName(), clazz.getName());
            }
        }
    }
}
