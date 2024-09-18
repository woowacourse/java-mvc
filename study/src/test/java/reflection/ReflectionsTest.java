package reflection;

import java.lang.annotation.Annotation;
import java.util.Map;
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
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Reflections reflections = new Reflections("reflection.examples");
        Map<Class<? extends Annotation>, String> classes = Map.of(
                Controller.class, "@Controller",
                Service.class, "@Service",
                Repository.class, "@Repository"
        );

        classes.forEach((annotationClass, annotationName) -> {
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotationClass);
            annotatedClasses.forEach(clazz -> log.info("{} : {}", annotationName, clazz.getName()));
        });
    }
}
