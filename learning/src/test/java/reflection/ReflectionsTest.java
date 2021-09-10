package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ReflectionsTest {

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> typesAnnotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> typesAnnotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);

        annotationClassesCheck(typesAnnotatedWithController, List.of("QnaController"));
        annotationClassesCheck(typesAnnotatedWithService, List.of("MyQnaService"));
        annotationClassesCheck(typesAnnotatedWithRepository, Arrays.asList("JdbcQuestionRepository", "JdbcUserRepository"));
    }

    private void annotationClassesCheck(final Set<Class<?>> typesAnnotatedWithController, final List<String> classNames) {
        List<String> controllerNames = typesAnnotatedWithController.stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toList());

        assertThat(controllerNames).containsExactlyInAnyOrderElementsOf(classNames);
    }
}
