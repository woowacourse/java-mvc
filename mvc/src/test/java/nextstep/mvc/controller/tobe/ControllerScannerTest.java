package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        controllerScanner = new ControllerScanner("samples");
    }

    @DisplayName("controller 애노테이션을 가진 클래스를 스캔한다.")
    @Test
    void controller_애노테이션을_가진_클래스를_스캔한다() {
        // given & when
        Set<Class<?>> actual = controllerScanner.getControllers();

        // then
        assertThat(actual).hasSize(1);
    }
}
