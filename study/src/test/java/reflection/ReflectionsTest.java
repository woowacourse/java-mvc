package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> controllerAnnotatedTypes = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceAnnotatedTypes = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryAnnotatedTypes = reflections.getTypesAnnotatedWith(Repository.class);

        log.info("ControllerAnnotatedTypes : {}", controllerAnnotatedTypes);
        log.info("ServiceAnnotatedTypes : {}", serviceAnnotatedTypes);
        log.info("RepositoryAnnotatedTypes : {}", repositoryAnnotatedTypes);

        assertAll(
                () -> assertThat(controllerAnnotatedTypes).hasSize(1),
                () -> assertThat(serviceAnnotatedTypes).hasSize(1),
                () -> assertThat(repositoryAnnotatedTypes).hasSize(2)
        );
    }
}
