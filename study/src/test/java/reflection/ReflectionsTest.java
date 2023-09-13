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
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            log.info("controllerClass = {}", controllerClass.getName());
        }

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> serviceClass : serviceClasses) {
            log.info("serviceClass = {}", serviceClass.getName());
        }

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> repositoryClass : repositoryClasses) {
            log.info("repositoryClass = {}", repositoryClass.getName());
        }
    }
}
