package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        var controllerClasses = reflections.getTypesAnnotatedWith(reflection.annotation.Controller.class);
        var serviceClasses = reflections.getTypesAnnotatedWith(reflection.annotation.Service.class);
        var repositoryClasses = reflections.getTypesAnnotatedWith(reflection.annotation.Repository.class);
        
        log.info("Controller classes:");
        controllerClasses.forEach(clazz -> log.info(" - {}", clazz.getName()));
        
        log.info("Service classes:");
        serviceClasses.forEach(clazz -> log.info(" - {}", clazz.getName()));
        
        log.info("Repository classes:");
        repositoryClasses.forEach(clazz -> log.info(" - {}", clazz.getName()));
    }
}
