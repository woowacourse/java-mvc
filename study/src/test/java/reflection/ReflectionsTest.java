package reflection;

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

        // @Controller 붙은 클래스
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(clazz -> log.info("Controller: {}", clazz.getName()));

        // @Service 붙은 클래스
        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(clazz -> log.info("Service: {}", clazz.getName()));

        // @Repository 붙은 클래스
        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(clazz -> log.info("Repository: {}", clazz.getName()));

        // [Test worker] INFO org.reflections.Reflections -- Reflections took 14 ms to scan 1 urls, producing 5 keys and 6 values
        // [Test worker] INFO reflection.ReflectionsTest -- Controller: reflection.examples.QnaController
        // [Test worker] INFO reflection.ReflectionsTest -- Service: reflection.examples.MyQnaService
        // [Test worker] INFO reflection.ReflectionsTest -- Repository: reflection.examples.JdbcQuestionRepository
        // [Test worker] INFO reflection.ReflectionsTest -- Repository: reflection.examples.JdbcUserRepository
    }
}
