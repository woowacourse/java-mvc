package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Assertions;
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

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClass = reflections.getTypesAnnotatedWith(Repository.class);

        Assertions.assertAll(
                () -> assertThat(controllerClass).isNotNull(),
                () -> assertThat(serviceClass).isNotNull(),
                () -> assertThat(repositoryClass).isNotNull()
        );

        log.info(controllerClass.toString());
        log.info(serviceClass.toString());
        log.info(repositoryClass.toString());
    }
}
