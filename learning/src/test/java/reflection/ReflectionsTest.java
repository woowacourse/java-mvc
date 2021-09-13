package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @DisplayName(" 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다")
    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");
        logging(reflections, Controller.class);
        logging(reflections, Service.class);
        logging(reflections, Repository.class);
    }

    private void logging(Reflections reflections, Class<? extends Annotation> annotationClass) {
        log.debug("@{}", annotationClass.getSimpleName());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotationClass);
        for (Class<?> aClass : classes) {
            log.debug("{}", aClass.getSimpleName());
        }
    }
}
