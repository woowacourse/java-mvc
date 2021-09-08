package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        Set<Class<?>> annotations = reflections.getTypesAnnotatedWith(Controller.class);
        annotations.addAll(reflections.getTypesAnnotatedWith(Service.class));
        annotations.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        // 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        for (Class<?> annotation : annotations) {
            LOG.debug(annotation.getSimpleName());
        }
    }
}
