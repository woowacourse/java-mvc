package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ControllerScannerTest {

    @Test
    void 패키지_내부의_컨트롤러_애너테이션이_적용된_클래스를_전부_반환한다() {
        // given
        final String basePackage = "samples";
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // when
        final Map<Class<?>, Object> result = controllerScanner.getControllers();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 패키지_내부의_컨트롤러_애너테이션이_적용된_클래스의_타입과_RequestMapping_Uri로_구성된_맵을_반환한다() {
        // given
        final String basePackage = "samples";
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // when
        final Map<Class<?>, String> uriPrefixes = controllerScanner.getUriPrefixes();

        // then
        assertThat(uriPrefixes.get(TestController.class)).isEqualTo("/test");
    }
}
