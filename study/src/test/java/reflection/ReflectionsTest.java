package reflection;

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
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> annotatedController : annotatedControllers) {
            log.debug(annotatedController.toString());
        }
        Set<Class<?>> annotatedServices = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> annotatedService : annotatedServices) {
            log.debug(annotatedService.toString());
        }
        Set<Class<?>> annotatedRepositories = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> annotatedRepository : annotatedRepositories) {
            log.debug(annotatedRepository.toString());
        }
    }
}
