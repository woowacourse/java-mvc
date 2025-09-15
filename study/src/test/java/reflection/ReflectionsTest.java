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
        // "reflection.examples" 패키지를 스캔 대상으로 하는 Reflections 객체 생성하기
        Reflections reflections = new Reflections("reflection.examples");
        // @Controller, @Service, @Repository 어노테이션 붙은 클래스들 찾기
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        log.debug("Found annotated classes: {}", annotatedClasses);
        for (Class<?> clazz : annotatedClasses) {
            log.info("Annotated class: {}", clazz.getName());
        }
    }
}
