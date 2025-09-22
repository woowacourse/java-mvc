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
        
        log.debug(reflections.getTypesAnnotatedWith(Controller.class).toString());
        log.debug(reflections.getTypesAnnotatedWith(Service.class).toString());
        log.debug(reflections.getTypesAnnotatedWith(Repository.class).toString());
    }
}
