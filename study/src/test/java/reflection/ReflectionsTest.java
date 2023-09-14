package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.reflect.Constructor;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        // @Controller 애노테이션이 설정된 클래스 찾기
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("@Controller classes:");
        for (Class<?> controllerClass : controllerClasses) {
            log.info(controllerClass.getName());
        }

        // @Service 애노테이션이 설정된 클래스 찾기
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        log.info("@Service classes:");
        for (Class<?> serviceClass : serviceClasses) {
            log.info(serviceClass.getName());
        }

        // @Repository 애노테이션이 설정된 클래스 찾기
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("@Repository classes:");
        for (Class<?> repositoryClass : repositoryClasses) {
            log.info(repositoryClass.getName());
        }
    }
}
