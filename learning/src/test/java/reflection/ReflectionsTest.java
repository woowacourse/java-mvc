package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionsTest.class);
    private static final Reflections REFLECTIONS = new Reflections("examples");

    @DisplayName("클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.")
    @Test
    void showAnnotationClass() {
        loggingWithAnotatedClass(REFLECTIONS.getTypesAnnotatedWith(Controller.class), Controller.class);
        loggingWithAnotatedClass(REFLECTIONS.getTypesAnnotatedWith(Service.class), Service.class);
        loggingWithAnotatedClass(REFLECTIONS.getTypesAnnotatedWith(Repository.class), Repository.class);
    }

    private void loggingWithAnotatedClass(Set<Class<?>> controllerAnnotatedWith, Class<?> annotation) {
        for (Class<?> aClass : controllerAnnotatedWith) {
            LOG.debug("애노테이션 = {}, 클래스 = {}", annotation.getName(), aClass.getName());
        }
    }

}
