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

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> annotatedClasses = new HashSet<>();

        // 각 애노테이션으로 클래스 검색 및 결과 합치기
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        // 찾은 클래스 목록 출력
        annotatedClasses.forEach(clazz -> System.out.println(clazz.getName()));

    }
}
