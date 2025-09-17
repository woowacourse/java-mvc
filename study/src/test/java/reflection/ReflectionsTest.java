package reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

    // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    @Test
    void showAnnotationClass() throws Exception {
        // reflection.examples 패키지를 기반으로 리플렉션 생성
        Reflections reflections = new Reflections("reflection.examples");

        // reflections.getTypesAnnotatedWith(애노테이션클래스)
        // - 해당 패키지내에서 특정 애노테이션이 붙은 클래스를 가져온다.
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClass = reflections.getTypesAnnotatedWith(Repository.class);

        controllerClass.forEach(clazz->log.info("@Controller class : {}", clazz.getCanonicalName()));
        serviceClass.forEach(clazz->log.info("@Service class : {}", clazz.getCanonicalName()));
        repositoryClass.forEach(clazz->log.info("@Repository class : {}", clazz.getCanonicalName()));
    }
}
