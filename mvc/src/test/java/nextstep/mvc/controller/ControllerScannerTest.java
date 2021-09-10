package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("패키지 경로를주면, @Controller 어노테이션이 붙은 클래스들을 찾아서 보관한다.")
    @Test
    void controllerScannerTest() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        annotationClassesCheck(controllers.keySet(), List.of("TestController"));
    }

    private void annotationClassesCheck(final Set<Class<?>> typesAnnotatedWithController, final List<String> classNames) {
        List<String> controllerNames = typesAnnotatedWithController.stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toList());

        assertThat(controllerNames).containsExactlyInAnyOrderElementsOf(classNames);
    }
}