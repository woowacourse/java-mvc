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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> controllerClazzSet = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : controllerClazzSet) {
            log.info("@Controller가 설정된 클래스 : {}", clazz.getSimpleName());
        }

        final Set<Class<?>> serviceClazzSet = reflections.getTypesAnnotatedWith(Service.class);
        for (final Class<?> clazz : serviceClazzSet) {
            log.info("@Service가 설정된 클래스 : {}", clazz.getSimpleName());
        }

        final Set<Class<?>> repositoryClazzSet = reflections.getTypesAnnotatedWith(Repository.class);
        for (final Class<?> clazz : repositoryClazzSet) {
            log.info("@Repository가 설정된 클래스 : {}", clazz.getSimpleName());
        }

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
