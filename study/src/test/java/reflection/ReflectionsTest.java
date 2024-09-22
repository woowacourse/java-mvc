package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        // TODO 클래스 레벨에 @Contrller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        controllerClasses.forEach(controller -> log.info(controller.getSimpleName()));
        serviceClasses.forEach(service -> log.info(service.getSimpleName()));
        repositoryClasses.forEach(repository -> log.info(repository.getSimpleName()));

        assertThat(controllerClasses).hasSize(1);
        assertThat(serviceClasses).hasSize(1);
        assertThat(repositoryClasses).hasSize(2);
    }
}
