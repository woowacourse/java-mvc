package reflection;

import java.util.Set;

import org.junit.jupiter.api.Test;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        Reflections reflections = new Reflections("examples");

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));

        classes.forEach(clazz -> System.out.println(clazz.getName()));
    }
}
