package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerScannerTest {

    @Test
    void base_패키지들을_입력해서_생성한다() {
        // given
        String basePackage = "samples";

        // expect
        assertThatNoException().isThrownBy(() -> ControllerScanner.from(basePackage));
    }

    @Test
    void base_패키지의_Controller_Annotated_클래스들을_반환한다() {
        // given
        String basePackage = "samples";
        ControllerScanner controllerScanner = ControllerScanner.from(basePackage);

        // when
        Map<Class<?>, Object> controllers = controllerScanner.controllers();

        // then
        assertThat(controllers.get(TestAnnotationController.class)).isNotNull();
    }
}
