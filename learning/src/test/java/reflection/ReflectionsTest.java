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

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        controllers.forEach(controller -> log.info(controller.getCanonicalName()));
        services.forEach(service -> log.info(service.getCanonicalName()));
        repositories.forEach(repository -> log.info(repository.getCanonicalName()));

        assertThat(controllers)
            .extracting("name")
            .containsExactly("examples.QnaController");
        assertThat(services)
            .extracting("name")
            .containsExactly("examples.MyQnaService");
        assertThat(repositories)
            .extracting("name")
            .contains("examples.JdbcQuestionRepository", "examples.JdbcUserRepository");
    }
}
