package reflection;

import java.util.List;
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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        List<String> controllerNames = controllers.stream()
                .map(Class::getSimpleName)
                .toList();
        List<String> serviceNames = services.stream()
                .map(Class::getSimpleName)
                .toList();
        List<String> repositoryNames = repositories.stream()
                .map(Class::getSimpleName)
                .toList();

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        log.info("Controller: {}", controllerNames);
        log.info("Service: {}", serviceNames);
        log.info("Repository: {}", repositoryNames);
    }
}
