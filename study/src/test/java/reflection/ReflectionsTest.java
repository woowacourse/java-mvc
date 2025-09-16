package reflection;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> clazz = new HashSet<>();
        clazz.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        clazz.addAll(reflections.getTypesAnnotatedWith(Service.class));
        clazz.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        for(Class<?> annotationClass : clazz) {
            log.info(annotationClass.getSimpleName());
        }
    }
}
