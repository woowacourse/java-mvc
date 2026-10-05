package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

class ReflectionsTest {

    //service, repository
    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        var services = reflections.getTypesAnnotatedWith(Service.class);
        var repositories = reflections.getTypesAnnotatedWith(Repository.class);

        for(var clazz : controllers){
            log.info("Controller: {}", clazz.getName());
        }

        for(var clazz : services){
            log.info("Service: {}", clazz.getName());
        }

        for(var clazz : repositories){
            log.info("Repository: {}", clazz.getName());
        }
    }
}
