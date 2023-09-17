package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            log.info("Controller: {}", controller.getName());
        }

        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        for (final Class<?> service : services) {
            log.info("Service: {}", service.getName());
        }

        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        for (final Class<?> repository : repositories) {
            log.info("Repository: {}", repository.getName());
        }

        // 전체 조회
        reflections.get(TypesAnnotated.of(Controller.class, Service.class, Repository.class))
                .forEach(object -> log.info("Object: {}", object));
    }
}
