package reflection;

import java.util.Set;

import org.junit.jupiter.api.Test;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest extends Output {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        log.info("[@Controller 클래스]");
        controllerClasses.forEach(c -> log.info(c.getName()));

        log.info("[@Service 클래스]");
        serviceClasses.forEach(c -> log.info(c.getName()));

        log.info("[@Repository 클래스]");
        repositoryClasses.forEach(c -> log.info(c.getName()));

        String output = captor.toString().trim();

        assertThat(output).contains("examples.QnaController");
        assertThat(output).contains("examples.MyQnaService");
        assertThat(output).contains("examples.JdbcUserRepository");
        assertThat(output).contains("examples.JdbcQuestionRepository");
    }
}
