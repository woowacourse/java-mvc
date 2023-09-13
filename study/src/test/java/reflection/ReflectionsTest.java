package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;
import reflection.examples.JdbcQuestionRepository;
import reflection.examples.JdbcUserRepository;
import reflection.examples.MyQnaService;
import reflection.examples.QnaController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        List<Class<?>> clazzes = Stream.of(Controller.class, Service.class, Repository.class)
                .flatMap(annotation -> reflections.getTypesAnnotatedWith(annotation).stream())
                .distinct()
                .collect(Collectors.toList());

        assertThat(clazzes).containsExactlyInAnyOrder(
                QnaController.class,
                MyQnaService.class,
                JdbcUserRepository.class,
                JdbcQuestionRepository.class
        );
    }
}
