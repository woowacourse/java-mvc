package reflection;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        log.info("--- @Controller 클래스 목록 ---");
        reflections.getTypesAnnotatedWith(reflection.annotation.Controller.class)
                .forEach(clazz -> log.info(clazz.getName()));

        log.info("--- @Service 클래스 목록 ---");
        reflections.getTypesAnnotatedWith(reflection.annotation.Service.class) // 패키지 경로에 맞게 수정 필요
                .forEach(clazz -> log.info(clazz.getName()));

        log.info("--- @Repository 클래스 목록 ---");
        reflections.getTypesAnnotatedWith(reflection.annotation.Repository.class) // 패키지 경로에 맞게 수정 필요
                .forEach(clazz -> log.info(clazz.getName()));

    }
}
