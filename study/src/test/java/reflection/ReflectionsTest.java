package reflection;

import java.util.HashSet;
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

        // 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        for (Class<?> clazz : classes) {
            log.info(clazz.getName());
        }
    }
}
