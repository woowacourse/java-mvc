package reflection;

import java.beans.ConstructorProperties;
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
        log.info("Controllers:");
        for(Class<?> controller : controllers) {
            log.info(controller.getName());
        }

        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        log.info("Services:");
        for(Class<?> service : services) {
            log.info(service.getName());
        }

        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("Repositories:");
        for(Class<?> repository : repositories) {
            log.info(repository.getName());
        }

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
