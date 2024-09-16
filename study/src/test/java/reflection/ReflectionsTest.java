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
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("controller classes : {}",controllerClasses);
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        log.info("controller classes : {}",serviceClasses);
        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("controller classes : {}",repositoryClasses);

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        /* Output is...
            15:23:11.084 [Test worker] INFO reflection.ReflectionsTest -- controller classes : [class reflection.examples.QnaController]
            15:23:11.085 [Test worker] INFO reflection.ReflectionsTest -- controller classes : [class reflection.examples.MyQnaService]
            15:23:11.086 [Test worker] INFO reflection.ReflectionsTest -- controller classes : [class reflection.examples.JdbcQuestionRepository, class reflection.examples.JdbcUserRepository]
         */
    }
}
