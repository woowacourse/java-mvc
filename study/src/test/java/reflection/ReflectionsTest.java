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
        // 리플랙션을 할 때 특정 패키지 내의 클래스를 조회한다던지 하려면 기본 리플랙션 API로는 상당히 복잡하다. 이걸 해주는 API다.

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        reflections.getTypesAnnotatedWith(Controller.class).forEach(aClass -> log.info(aClass.toString()));

        reflections.getTypesAnnotatedWith(Service.class).forEach(aClass -> log.info(aClass.toString()));

        reflections.getTypesAnnotatedWith(Repository.class).forEach(aClass -> log.info(aClass.toString()));
    }
}
