package nextstep.mvc.controller.scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller Scanner는 BasePackage를 기준으로 @Controller가 사용된 모든 클래스를 찾는다.")
    void findAllClassesOfAnnotationController() {
        // given & when
        Map<Class<?>, Object> controllers = ControllerScanner.getInstance()
            .getControllers("samples");

        // then
        List<String> extract = controllers.keySet().stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(extract).contains("TestController")
        );
    }
}
