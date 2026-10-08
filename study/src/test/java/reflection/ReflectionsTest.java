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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        controllers.forEach(clazz -> log.debug("@Controller: {}", clazz.getName()));
        services.forEach(clazz -> log.debug("@Service: {}", clazz.getName()));
        repositories.forEach(clazz -> log.debug("@Repository: {}", clazz.getName()));

        assertThat(controllers).containsExactlyInAnyOrder(QnaController.class);
        assertThat(services).containsExactlyInAnyOrder(MyQnaService.class);
        assertThat(repositories).containsExactlyInAnyOrder(JdbcQuestionRepository.class, JdbcUserRepository.class);
    }
}
