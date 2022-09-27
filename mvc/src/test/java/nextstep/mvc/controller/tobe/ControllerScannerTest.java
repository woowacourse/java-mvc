package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @Test
    void getControllers() {
        // given
        final Reflections reflections = new Reflections("samples");
        final ControllerScanner scanner = new ControllerScanner(reflections);

        // when
        final Map<Class<?>, Object> actual = scanner.getControllers();

        // then
        assertThat(actual).hasSize(1);
        String name = actual.keySet().stream()
                .findFirst()
                .get()
                .getName();
        assertThat(name).isEqualTo("samples.TestController");
    }
}
