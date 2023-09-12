package reflection;

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
        /**
         * getTypesAnnotatedWith -> 주어진 패키지나 클래스 경로에서 특정 애노테이션이 달린 클래스들을 검색하는 데 사용됨
         * honorInherited: 상속된 어노테이션을 존중할지 여부
         */
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class, true);
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Service.class, true));
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class, true));

        for (final Class<?> clazz : annotatedClasses) {
            log.info(clazz.getName());
        }
    }
}
