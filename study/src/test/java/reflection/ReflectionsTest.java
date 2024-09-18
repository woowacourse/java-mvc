package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.List;
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
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");

        List<Class<? extends Annotation>> annotationClasses = List.of(Controller.class,
            Service.class, Repository.class);

        int count = annotationClasses.stream()
            .mapToInt(annotation -> logAnnotatedClasses(reflections, annotation))
            .sum();

        assertThat(count).isEqualTo(4);
    }

    private int logAnnotatedClasses(Reflections reflections,
        Class<? extends Annotation> annotation) {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation);

        annotatedClasses.forEach(
            clazz -> log.info("@{}: {}", annotation.getSimpleName(), clazz.getName()));

        return annotatedClasses.size();
    }
}
