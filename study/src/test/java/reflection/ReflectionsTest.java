package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        for (final Class<?> controller : controllers) {
            log.info("controllers: {}", controller.getSimpleName());
        }
        for (final Class<?> service : services) {
            log.info("services: {}", service.getSimpleName());
        }
        for (final Class<?> repository : repositories) {
            log.info("repositories: {}", repository.getSimpleName());
        }
    }
}
