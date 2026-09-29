package reflection;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("특정 애노테이션이 포함된 클래스를 조회할 수 있다.")
    void showAnnotationClass() {
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        // Reflections는 특정 패키지에서 조건에 맞는 클래스나 메서드를 찾을 때 사용할 수 있는 외부 라이브러리이다.
        Reflections reflections = new Reflections("reflection.examples");

        // Reflections로 여러 클래스에서 검색 -> 자바 리플렉션을 이용해 처리

        // 어노테이션은 내부적으로 Annotation 인터페이스를 상속받는다.
        List<Class<? extends Annotation>> annotations = List.of(Controller.class, Service.class, Repository.class);

        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
            for (Class<?> aClass : classes) {
                log.info("{}", aClass.getName());
            }
        }
    }
}
