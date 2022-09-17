package nextstep.mvc.controller.scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller Scanner는 BasePackage를 기준으로 @Controller가 사용된 모든 클래스를 찾는다.")
    void findAllClassesOfAnnotationController() {
        // given & when
        Set<Class<?>> classes = ControllerScanner.getInstance().getAllAnnotations(
            "nextstep.mvc.controller.scanner");

        // then
        List<String> classNames = classes.stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(classNames).hasSize(1),
            () -> assertThat(classNames).containsExactly("AnnotationController")
        );
    }

    @Controller
    private class AnnotationController {

    }

    private class NotAnnotationController {

    }
}
