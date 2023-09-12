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
    void showAnnotationClass() {
        final Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> classWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> classWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> classWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);

        for (final Class<?> clazz : classWithControllerAnnotation) {
            log.info("{} 클래스는 @Controller가 명시되어 있습니다.", clazz);
        }

        for (final Class<?> clazz : classWithServiceAnnotation) {
            log.info("{} 클래스는 @Service가 명시되어 있습니다.", clazz);
        }

        for (final Class<?> clazz : classWithRepositoryAnnotation) {
            log.info("{} 클래스는 @Repository가 명시되어 있습니다.", clazz);
        }
    }
}
