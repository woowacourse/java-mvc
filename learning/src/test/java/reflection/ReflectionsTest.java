package reflection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.junit.jupiter.api.Test;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {

        // given
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));

        Reflections reflections = new Reflections("examples");

        // when
        Set<Class<?>> controllerAnnotated = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceAnnotated = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryAnnotated = reflections.getTypesAnnotatedWith(Repository.class);

        controllerAnnotated.forEach(clazz -> log.info("Class annotated controller : {}", clazz.getSimpleName()));
        serviceAnnotated.forEach(clazz -> log.info("Class annotated service : {}", clazz.getSimpleName()));
        repositoryAnnotated.forEach(clazz -> log.info("Class annotated repository : {}", clazz.getSimpleName()));

        // then
        assertThat(captor.toString().trim()).contains("QnaController", "MyQnaService", "JdbcUserRepository", "JdbcQuestionRepository");
    }
}
