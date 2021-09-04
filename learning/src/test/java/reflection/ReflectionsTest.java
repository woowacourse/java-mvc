package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
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

        final Set<Class<?>> classesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> classWithControllerAnnotation : classesWithControllerAnnotation) {
            log.debug("@Controller 애노테이션이 설정되어있는 클래스 이름 : {}", classWithControllerAnnotation.getName());
        }

        final Set<Class<?>> classesWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> classWithServiceAnnotation : classesWithServiceAnnotation) {
            log.debug("@Service 애노테이션이 설정되어있는 클래스 이름 : {}", classWithServiceAnnotation.getName());
        }

        final Set<Class<?>> classesWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> classWithRepositoryAnnotation : classesWithRepositoryAnnotation) {
            log.debug("@Repository 애노테이션이 설정되어있는 클래스 이름 : {}", classWithRepositoryAnnotation.getName());
        }
    }
}
