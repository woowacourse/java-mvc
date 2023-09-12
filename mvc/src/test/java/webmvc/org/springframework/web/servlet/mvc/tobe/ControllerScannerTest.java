package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerScannerTest {

    @Test
    void 패키지_내부의_컨트롤러를_모두_반환한다() {
        // given
        final String basePackage = "samples";
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // when
        final Map<Class<?>, Object> result = controllerScanner.getControllers();

        // then
        assertThat(result).hasSize(1);
    }
}
