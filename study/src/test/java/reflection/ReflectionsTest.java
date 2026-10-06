package reflection;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @DisplayName("Reflections로 패키지를 스캔해 @Controller, @Service, @Repository가 붙은 클래스를 찾는다.")
    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> controllerClass : controllerClasses) {
            log.info("controllerClass: {}", controllerClass.getSimpleName());
        }

        for (Class<?> serviceClass : serviceClasses) {
            log.info("serviceClass: {}", serviceClass.getSimpleName());
        }

        for (Class<?> repositoryClass : repositoryClasses) {
            log.info("repositoryClass: {}", repositoryClass.getSimpleName());
        }
    }
}
