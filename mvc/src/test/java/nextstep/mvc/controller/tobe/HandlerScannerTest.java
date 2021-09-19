package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.controller.tobe.handler.mapping.HandlerScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerScannerTest {

    @DisplayName("모든 Handler(Controller)를 스캔한다.")
    @Test
    void scan() {
        // given
        HandlerScanner handlerScanner = new HandlerScanner();

        // when
        handlerScanner.scan(new Object[]{"samples"});

        // then
        assertThat(handlerScanner.getHandlers()).hasSize(1);
    }
}
