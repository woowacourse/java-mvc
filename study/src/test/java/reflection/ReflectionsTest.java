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

    /**
     * Reflections는 자바에서 리플렉션 기능을 사용하여 애플리케이션의 메타데이터를 검색하고 활용하는 라이브러리.
     * 이 라이브러리는 주로 애너테이션, 서브클래스, 메서드, 필드 등을 쉽게 찾기 위해 사용.
     * 매개변수로 검색할 패키지 경로를 넣는다.
     */
    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClass = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClass = reflections.getTypesAnnotatedWith(Repository.class);
        printClass(controllerClass);
        printClass(serviceClass);
        printClass(repositoryClass);
        /**
         * 17:59:12.891 [Test worker] INFO reflection.ReflectionsTest -- Class: reflection.examples.QnaController
         * 17:59:12.891 [Test worker] INFO reflection.ReflectionsTest -- Class: reflection.examples.MyQnaService
         * 17:59:12.891 [Test worker] INFO reflection.ReflectionsTest -- Class: reflection.examples.JdbcQuestionRepository
         * 17:59:12.891 [Test worker] INFO reflection.ReflectionsTest -- Class: reflection.examples.JdbcUserRepository
         */
    }

    private void printClass(Set<Class<?>> classSet) {
        for (Class<?> clazz : classSet) {
            log.info("Class: {}", clazz.getName());
        }
    }
}
