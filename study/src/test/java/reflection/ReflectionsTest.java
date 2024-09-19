package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Assertions;
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

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllers) {
            log.info(clazz.getSimpleName());
        }

        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> clazz : services) {
            log.info(clazz.getSimpleName());
        }

        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> clazz : repositories) {
            log.info(clazz.getSimpleName());
        }

        Assertions.assertAll(
                () -> assertThat(controllers).containsExactly(QnaController.class),
                () -> assertThat(services).containsExactly(MyQnaService.class),
                () -> assertThat(repositories).containsExactly(JdbcQuestionRepository.class, JdbcUserRepository.class)
        );

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 로그로 출력한다.
    }
}
