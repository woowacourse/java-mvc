package reflection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> typesAnnotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> typesAnnotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);

        Set.of(typesAnnotatedWithController, typesAnnotatedWithService, typesAnnotatedWithRepository)
                .forEach(classes -> classes.forEach(clazz -> log.info("Class: {}", clazz.getName())));

        assertAll(
                () -> assertEquals(Set.of(QnaController.class), typesAnnotatedWithController),
                () -> assertEquals(Set.of(MyQnaService.class), typesAnnotatedWithService),
                () -> assertEquals(Set.of(JdbcQuestionRepository.class, JdbcUserRepository.class), typesAnnotatedWithRepository)
        );
    }
}
