package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class ControllerScannerTest {

    @Test
    @DisplayName("특정 패키지에 @Controller가 붙은 클래스를 찾을 수 있다.")
    void findByPackage() {
        // given
        final String packageName = "samples";
        final ControllerScanner controllerScanner = ControllerScanner.getInstance();

        // when
        Map<Class<?>, Object> foundClazz = controllerScanner.scan(packageName);

        // then
        assertAll(
                () -> assertThat(foundClazz).hasSize(2),
                () -> assertThat(foundClazz)
                        .extractingByKey(TestAnnotationController.class)
                        .isInstanceOf(TestAnnotationController.class)
        );
    }

}
