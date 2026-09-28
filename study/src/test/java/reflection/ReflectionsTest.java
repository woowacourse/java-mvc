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

import java.lang.annotation.Annotation;
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

        logTypes(Controller.class, controllers);
        logTypes(Service.class, services);
        logTypes(Repository.class, repositories);

        assertThat(controllers).containsExactly(QnaController.class);
        assertThat(services).containsExactly(MyQnaService.class);
        assertThat(repositories).containsExactlyInAnyOrder(JdbcUserRepository.class, JdbcQuestionRepository.class);
    }

    private void logTypes(Class<? extends Annotation> annotation, Set<Class<?>> types) {
        types.forEach(type -> log.info("@{} - {}", annotation.getSimpleName(), type.getSimpleName()));
    }
}
