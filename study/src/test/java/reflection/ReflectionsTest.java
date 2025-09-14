package reflection;

import di.stage4.annotations.Repository;
import di.stage4.annotations.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        var controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        var serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        var repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        controllerClasses.forEach(c -> log.debug("Controller: {}", c.getName()));
        serviceClasses.forEach(c -> log.debug("Service: {}", c.getName()));
        repositoryClasses.forEach(c -> log.debug("Repository: {}", c.getName()));
    }
}
