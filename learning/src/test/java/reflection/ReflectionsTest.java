package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");

        final List<Class<? extends Annotation>> classes = Arrays.asList(
                Controller.class,
                Service.class,
                Repository.class
        );
        final List<String> clazz = classes.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

        assertThat(clazz).contains(
                "QnaController",
                "MyQnaService",
                "JdbcUserRepository",
                "JdbcQuestionRepository"
        );
    }
}
