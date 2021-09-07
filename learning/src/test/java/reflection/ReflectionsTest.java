package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import ch.qos.logback.core.boolex.EvaluationException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> typesAnnotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> typesAnnotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);

        logTypeAnnotatedWith(typesAnnotatedWithController);
        logTypeAnnotatedWith(typesAnnotatedWithService);
        logTypeAnnotatedWith(typesAnnotatedWithRepository);
    }

    private void logTypeAnnotatedWith(final Set<Class<?>> typeAnnotatedWith) {
        for (Class<?> annotated : typeAnnotatedWith) {
            log.debug("Annotated Class Name : {}", annotated.getSimpleName());
        }
    }
}
