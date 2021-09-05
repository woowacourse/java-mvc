package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        assertDoesNotThrow(() -> {
            // prefix - 패키지 prefix
            Reflections reflections = new Reflections("examples");

            // 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
            Class<Controller> controllerClazz = Controller.class;
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(controllerClazz);

            Class<Service> serviceClazz = Service.class;
            Set<Class<?>> services = reflections.getTypesAnnotatedWith(serviceClazz);

            Class<Repository> repositoryClazz = Repository.class;
            Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(repositoryClazz);

            logClazzName(controllerClazz, controllers);
            logClazzName(serviceClazz, services);
            logClazzName(repositoryClazz, repositories);
        });
    }

    private <T> void logClazzName(Class<T> annotationClazz, Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            log.debug("annotation {}, class : {}", annotationClazz.getName(), clazz.getSimpleName());
        }
    }
}
