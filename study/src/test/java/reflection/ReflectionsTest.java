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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        final Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        controllers.forEach(controller -> log.info("Controller: {}", controller.getName()));
        services.forEach(service -> log.info("Service: {}", service.getName()));
        repositories.forEach(repository -> log.info("Repository: {}", repository.getName()));

        assertThat(controllers).containsExactlyInAnyOrder(QnaController.class);
        assertThat(services).containsExactlyInAnyOrder(MyQnaService.class);
        assertThat(repositories).containsExactlyInAnyOrder(
                JdbcQuestionRepository.class, JdbcUserRepository.class);
    }
}
