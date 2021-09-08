package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        final Set<Class<?>> classesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        doLogging(Controller.class, classesWithControllerAnnotation);
        final Set<Class<?>> classesWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        doLogging(Service.class, classesWithServiceAnnotation);
        final Set<Class<?>> classesWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);
        doLogging(Repository.class, classesWithRepositoryAnnotation);
    }

    private void doLogging(Class<?> annotationClass, Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            log.debug("@{} 애노테이션이 설정되어있는 클래스 이름 : {}", annotationClass.getSimpleName(), clazz.getSimpleName());
        }
    }
}
