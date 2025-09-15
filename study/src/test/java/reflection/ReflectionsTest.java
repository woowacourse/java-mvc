package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        final var controllerClasses = reflections.getTypesAnnotatedWith(reflection.annotation.Controller.class);
        final var serviceClasses = reflections.getTypesAnnotatedWith(reflection.annotation.Service.class);
        final var repositoryClasses = reflections.getTypesAnnotatedWith(reflection.annotation.Repository.class);

        for (final var clazz : controllerClasses) {
            log.debug("Controller: {}", clazz.getName());
        }
        for (final var clazz : serviceClasses) {
            log.debug("Service: {}", clazz.getName());
        }
        for (final var clazz : repositoryClasses) {
            log.debug("Repository: {}", clazz.getName());
        }
    }
}
