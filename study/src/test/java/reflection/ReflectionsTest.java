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
    @DisplayName("클레스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정된 모든 클래스를 출력한다")
    void showAnnotationClass() throws Exception {
        // given
        List<Class<? extends Annotation>> targetAnnotations = List.of(
                Controller.class, Service.class, Repository.class
        );
        Reflections reflections = new Reflections("reflection.examples");

        // when and then
        for (Class<? extends Annotation> annotation : targetAnnotations) {
            Set<Class<?>> foundTypes = reflections.getTypesAnnotatedWith(annotation);
            for (Class<?> type : foundTypes) {
                log.info("@{} {} in {}",
                        "%-12s".formatted(annotation.getSimpleName()),
                        "%-25s".formatted(type.getSimpleName()),
                        type.getPackageName()
                );
            }
        }
    }
}
