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

        final Set<Class<?>> controllerAnnotatedClazz = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> serviceAnnotatedClazz = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositoryAnnotatedClazz = reflections.getTypesAnnotatedWith(Repository.class);

        log.info("controllerAnnotatedClazz: {}", controllerAnnotatedClazz);
        log.info("serviceAnnotatedClazz: {}", serviceAnnotatedClazz);
        log.info("repositoryAnnotatedClazz: {}", repositoryAnnotatedClazz);

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
