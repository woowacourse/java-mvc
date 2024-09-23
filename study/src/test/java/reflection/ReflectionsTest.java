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

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(controller -> log.info("controller : {}", controller));
        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(service -> log.info("service : {}", service));
        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(repository -> log.info("repository : {}", repository));
    }
}
