package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;
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
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        log.info("classes with @Controller: {}", controllerClasses);
        log.info("classes with @Service: {}", serviceClasses);
        log.info("classes with @Repository: {}", repositoryClasses);

        assertAll(
                () -> assertThat(controllerClasses).hasSize(1).contains(QnaController.class),
                () -> assertThat(serviceClasses).hasSize(1).contains(MyQnaService.class),
                () -> assertThat(repositoryClasses).hasSize(2)
                        .contains(
                                JdbcQuestionRepository.class,
                                JdbcUserRepository.class
                        )
        );
    }
}
