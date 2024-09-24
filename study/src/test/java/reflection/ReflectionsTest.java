package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    @DisplayName("클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그 출력")
    void showAnnotationClass() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Reflections reflections = new Reflections("reflection.examples");

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(aClass -> log.info("[@Controller]" + aClass.getName()));
        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(aClass -> log.info("[@Service]" + aClass.getName()));
        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(aClass -> log.info("[@Repository]" + aClass.getName()));

        assertThat(outContent.toString()).contains(
                "[@Controller]reflection.examples.QnaController",
                "[@Service]reflection.examples.MyQnaService",
                "[@Repository]reflection.examples.JdbcQuestionRepository",
                "[@Repository]reflection.examples.JdbcUserRepository"
        );

        System.setOut(originalOut);
    }
}
