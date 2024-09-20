package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
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
    private static final String EXAMPLES_CONTROLLER = "QnaController";
    private static final String EXAMPLES_SERVICE = "MyQnaService";
    private static final String EXAMPLES_REPOSITORY_1 = "JdbcQuestionRepository";
    private static final String EXAMPLES_REPOSITORY_2 = "JdbcUserRepository";

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @DisplayName("클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.")
    @Test
    void showAnnotationClass() throws Exception {
        List<String> rawControllerClasses = new ArrayList<>();
        List<String> rawServiceClasses = new ArrayList<>();
        List<String> rawRepositoryClasses = new ArrayList<>();

        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("<Controller Class>");
        controllerClasses.forEach(clazz -> log.info(clazz.getSimpleName()));
        controllerClasses.forEach(clazz -> rawControllerClasses.add(clazz.getSimpleName()));

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        log.info("<Service Class>");
        serviceClasses.forEach(clazz -> log.info(clazz.getSimpleName()));
        serviceClasses.forEach(clazz -> rawServiceClasses.add(clazz.getSimpleName()));

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("<Repository Class>");
        repositoryClasses.forEach(clazz -> log.info(clazz.getSimpleName()));
        repositoryClasses.forEach(clazz -> rawRepositoryClasses.add(clazz.getSimpleName()));

        assertAll(
                () -> assertThat(rawControllerClasses).contains(EXAMPLES_CONTROLLER),
                () -> assertThat(rawServiceClasses).contains(EXAMPLES_SERVICE),
                () -> assertThat(rawRepositoryClasses).contains(EXAMPLES_REPOSITORY_1, EXAMPLES_REPOSITORY_2)
        );
    }
}
