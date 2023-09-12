package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ControllerScannerTest {

    @Test
    void Controller_어노테이션이_붙은_클래스를_반환한다() throws Exception {
        final var controllerScanner = new ControllerScanner(new Object[]{"samples"});

        var controllers = controllerScanner.getControllers();

        assertThat(controllers).hasSize(1);
    }

    @Test
    void Controller_어노테이션이_없는_클래스를_반환하지_않는다() throws Exception {
        final var controllerScanner = new ControllerScanner(new Object[]{"webmvc.org.springframework.web.servlet.mvc.tobe"});

        var controllers = controllerScanner.getControllers();

        assertThat(controllers).isEmpty();
    }
}
