package reflection;

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

        // @Controller가 붙은 클래스
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(clazz -> log.info("Controller class: {}", clazz.getName()));

        // @Service가 붙은 클래스
        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(clazz -> log.info("Service class: {}", clazz.getName()));

        // @Repository가 붙은 클래스
        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(clazz -> log.info("Repository class: {}", clazz.getName()));
    }
}
