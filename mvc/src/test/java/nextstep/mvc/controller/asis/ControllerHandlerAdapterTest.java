package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @DisplayName("adapter에서 지원하는 handler일 경우 true를 반환한다.")
    @Test
    void supports_true() {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final Controller forwardController = new ForwardController("");

        // when
        final boolean actual = controllerHandlerAdapter.supports(forwardController);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("adapter에서 지원하지 않는 handler일 경우 false를 반환한다.")
    @Test
    void supports_false() {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        // when
        final boolean actual = controllerHandlerAdapter.supports(controllerHandlerAdapter);

        // then
        assertThat(actual).isFalse();
    }
}
