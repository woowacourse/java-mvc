package reflection;

import examples.annotations.Component;
import examples.annotations.Controller;
import examples.annotations.Repository;
import examples.annotations.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> typesAnnotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> typesAnnotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);

        typesAnnotatedWithController.forEach(clazz -> log.info("class : {}", clazz));
        typesAnnotatedWithService.forEach(clazz -> log.info("class : {}", clazz));
        typesAnnotatedWithRepository.forEach(clazz -> log.info("class : {}", clazz));

        System.out.println("====================");
        log.info("get With Component Annotation");

        Set<Class<?>> typesAnnotatedWithComponent2 = reflections.getTypesAnnotatedWith(Component.class);

        typesAnnotatedWithComponent2.forEach(clazz -> log.info("class : {}", clazz));
    }
}
