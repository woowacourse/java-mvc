package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        // 패키지 경로와 함께 객체를 생성한다.
        Reflections reflections = new Reflections("examples");

        // 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        for (Class<? extends Annotation> clazz : List.of(Controller.class, Service.class, Repository.class)) {
            log.info("For {} classes,", clazz.getSimpleName());
            reflections.getTypesAnnotatedWith(clazz)
                    .forEach(instance -> log.info("{} has been found.", instance.getSimpleName()));
        }

    }
}
