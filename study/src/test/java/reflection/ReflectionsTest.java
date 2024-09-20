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

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        typesAnnotatedWithController
                .forEach(clazz -> log.info("Controller: {}", clazz.getName()));
        Set<Class<?>> typesAnnotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        typesAnnotatedWithService
                .forEach(clazz -> log.info("Service: {}", clazz.getName()));
        Set<Class<?>> typesAnnotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);
        typesAnnotatedWithRepository
                .forEach(clazz -> log.info("Repository: {}", clazz.getName()));

        assertAll(
                () -> assertEquals(1, typesAnnotatedWithController.size()),
                () -> assertEquals(1, typesAnnotatedWithService.size()),
                () -> assertEquals(2, typesAnnotatedWithRepository.size())
        );
    }
}
