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
        // Reflections : 패키지 전체를 스캔하는 외부 라이브러리
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        // getTypesAnnotatedWith() : 해당 애노테이션이 클래스 레벨에 붙은 클래스를 Set<Class<?>> 로 반환
        logClassNames("@Controller", reflections.getTypesAnnotatedWith(Controller.class));
        logClassNames("@Service", reflections.getTypesAnnotatedWith(Service.class));
        logClassNames("@Repository", reflections.getTypesAnnotatedWith(Repository.class));
    }

    private void logClassNames(final String annotationName, final Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            log.info("{} : {}", annotationName, clazz.getName());
        }
    }
}
