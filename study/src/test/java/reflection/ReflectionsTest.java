package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

class ReflectionsTest extends OutputStreamTestSetup {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        // given
        final List<Class<? extends Annotation>> annotations = List.of(
                Controller.class,
                Service.class,
                Repository.class
        );
        final Reflections reflections = new Reflections("reflection.examples");

        // when
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        annotations.stream().map(reflections::getTypesAnnotatedWith).forEach(annotatedClasses ->
                annotatedClasses.forEach(annoatatedClass -> log.info(annoatatedClass.getSimpleName()))
        );

        // then
        assertThat(out.toString()).contains(
                "QnaController",
                "MyQnaService",
                "JdbcQuestionRepository",
                "JdbcUserRepository"
        );
    }
}
