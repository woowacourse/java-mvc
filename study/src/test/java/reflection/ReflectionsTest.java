package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;
import reflection.examples.JdbcQuestionRepository;
import reflection.examples.JdbcUserRepository;
import reflection.examples.MyQnaService;
import reflection.examples.QnaController;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        HashSet<Class> result = new HashSet<>();
        result.addAll(reflections.getTypesAnnotatedWith(Service.class));
        result.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        result.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        assertAll(
            () -> assertThat(result).contains(JdbcQuestionRepository.class),
            () -> assertThat(result).contains(JdbcUserRepository.class),
            () -> assertThat(result).contains(MyQnaService.class),
            () -> assertThat(result).contains(QnaController.class)
        );
    }
}
