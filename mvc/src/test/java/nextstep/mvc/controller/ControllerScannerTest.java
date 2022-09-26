package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import nextstep.mvc.controller.ControllerScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("getControllers 메소드는 스캔한 @Controller 클래스와 해당 클래스의 인스턴스를 매핑한 결과를 반환한다.")
    void getControllers() {
        // given
        final ControllerScanner controllerScanner = ControllerScanner.from(new Object[]{"samples"});

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers)
                .extractingByKey(TestController.class)
                .isInstanceOf(TestController.class);
    }
}