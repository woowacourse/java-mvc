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

        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        final var services = reflections.getTypesAnnotatedWith(Service.class);
        final var repositories = reflections.getTypesAnnotatedWith(Repository.class);
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        for (var controller : controllers) {
            System.out.println("controller.getName() = " + controller.getName());
        }
        for (var service : services) {
            System.out.println("service.getName() = " + service.getName());
        }
        for (var repository : repositories) {
            System.out.println("repository.getName() = " + repository.getName());
        }
    }
}
