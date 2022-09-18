package nextstep.mvc.handlerAdapter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;

class ManualHandlerAdapterTest {

    @Test
    @DisplayName("Controller의 구현체라면 true를 return한다.")
    void handlerSupportsHandlerExecution() {
        Controller controller = mock(Controller.class);
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        assertThat(manualHandlerAdapter.supports(controller)).isTrue();
    }

    @Test
    @DisplayName("Controller의 구현체가 아니라면 false를 return한다.")
    void handlerNotSupport() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        assertThat(manualHandlerAdapter.supports(handlerExecution)).isFalse();
    }
}