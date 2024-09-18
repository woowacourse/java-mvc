package reflection;

import java.io.Serial;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(Serial.class));
        typesAnnotatedWith.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        for (Class<?> type : typesAnnotatedWith) {
            log.info(type.toString());
        }
    }
}
