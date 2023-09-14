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
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);

        controllerClasses.forEach(it -> log.info("class: {}", it.getName()));
        serviceClasses.forEach(it -> log.info("class: {}", it.getName()));
        repositoryClasses.forEach(it -> log.info("class: {}", it.getName()));
    }
}
