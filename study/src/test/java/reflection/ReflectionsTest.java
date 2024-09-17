package reflection;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

/*
 * Reflections 클래스 제공 기능
 * 1. 클래스 스캔: 특정 패키지 내의 모든 클래스를 검색할 수 있다.
 * 2. 애노테이션 검색: 애노테이션이 적용된 클래스, 메서드, 필드 등을 찾을 수 있다.
 * 3. 메서드 및 필드 검색: 특정 메서드 이름이나 필드를 가진 클래스를 찾을 수 있다.
 * 4. 종속성 주입: 애플리케이션의 메타데이터를 동적으로 검색하고 활용할 수 있다.
 */
class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {

        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            log.info("Controller Found: {}", controller.getName());
        }

        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> service : services) {
            log.info("Service Found: {}", service.getName());
        }

        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> repository : repositories) {
            log.info("Repository Found: {}", repository.getName());
        }
    }
}
