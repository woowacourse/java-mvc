package reflection;

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
        // reflection.examples 패키지를 대상으로 Reflections 객체 생성
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        // @Controller 애노테이션이 설정된 클래스 찾기
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("\n\n@Controller Classes:");
        controllerClasses.forEach(clazz -> log.info(clazz.getName()));

        // @Service 애노테이션이 설정된 클래스 찾기
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        log.info("\n\n@Service Classes:");
        serviceClasses.forEach(clazz -> log.info(clazz.getName()));

        // @Repository 애노테이션이 설정된 클래스 찾기
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("\n\n@Repository Classes:");
        repositoryClasses.forEach(clazz -> log.info(clazz.getName()));
    }
}
