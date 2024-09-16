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

    /*
    Reflections 라이브러리는.. 현재 활발하게 개발중이지 않은 것으로 보인다.
    버전도 0.10.2, 메이저가 아닌데 어떻게 쓸 수 있는거지 ?
     */
    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");
        Set<Class<?>> repositoryAnnotatedTypes = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> serviceAnnotatedTypes = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> controllerAnnotatedTypes = reflections.getTypesAnnotatedWith(Controller.class);

        log.info("repositoryAnnotatedTypes : {}", repositoryAnnotatedTypes);
        log.info("serviceAnnotatedTypes : {}", serviceAnnotatedTypes);
        log.info("controllerAnnotatedTypes : {}", controllerAnnotatedTypes);
        assertAll(
                () -> assertThat(repositoryAnnotatedTypes).hasSize(2),
                () -> assertThat(serviceAnnotatedTypes).hasSize(1),
                () -> assertThat(controllerAnnotatedTypes).hasSize(1)
        );
    }
}
