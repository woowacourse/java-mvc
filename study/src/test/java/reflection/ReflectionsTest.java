package reflection;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> controllerClasses = reflections.get(Scanners.TypesAnnotated.with(Controller.class).asClass());
        Set<Class<?>> serviceClasses = reflections.get(Scanners.TypesAnnotated.with(Service.class).asClass());
        Set<Class<?>> repositoryClasses = reflections.get(Scanners.TypesAnnotated.with(Repository.class).asClass());

        log.info("Controller classes:");
        controllerClasses.forEach(clazz -> log.info(clazz.getName()));

        log.info("Service classes:");
        serviceClasses.forEach(clazz -> log.info(clazz.getName()));

        log.info("Repository classes:");
        repositoryClasses.forEach(clazz -> log.info(clazz.getName()));
    }
}
