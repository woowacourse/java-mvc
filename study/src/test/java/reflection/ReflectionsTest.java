package reflection;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("패키지에서 @Controller가 붙은 클래스를 찾는다")
    void findsControllerClasses() {
        final Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(type -> log.info("Controller: {}", type.getName()));
        assertThat(controllers).containsExactlyInAnyOrder(QnaController.class);
    }

    @Test
    @DisplayName("패키지에서 @Service가 붙은 클래스를 찾는다")
    void findsServiceClasses() {
        final Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);

        services.forEach(type -> log.info("Service: {}", type.getName()));
        assertThat(services).containsExactlyInAnyOrder(MyQnaService.class);
    }

    @Test
    @DisplayName("패키지에서 @Repository가 붙은 클래스를 찾는다")
    void findsRepositoryClasses() {
        final Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        repositories.forEach(type -> log.info("Repository: {}", type.getName()));
        assertThat(repositories).containsExactlyInAnyOrder(
                JdbcQuestionRepository.class, JdbcUserRepository.class);
    }
}
