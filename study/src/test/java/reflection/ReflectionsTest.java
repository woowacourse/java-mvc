package reflection;

import jdk.jfr.ContentType;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.reflect.Constructor;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        for(Class<?> constructorsAnnotatedWith : typesAnnotatedWith){
            System.out.println(constructorsAnnotatedWith.getName());
        }

        typesAnnotatedWith = reflections.getTypesAnnotatedWith(Service.class);
        for(Class<?> constructorsAnnotatedWith : typesAnnotatedWith){
            System.out.println(constructorsAnnotatedWith.getName());
        }

        typesAnnotatedWith = reflections.getTypesAnnotatedWith(Repository.class);
        for(Class<?> constructorsAnnotatedWith : typesAnnotatedWith){
            System.out.println(constructorsAnnotatedWith.getName());
        }

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
