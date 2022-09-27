package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AnnotationHandlerAdapterTest {

    private static final HandlerAdapter HANDLER_ADAPTER = new AnnotationHandlerAdapter();

    @DisplayName("handle 할 수 있는 handler인지 반환한다 (true)")
    @Test
    void supports_True() {
        HandlerExecution handlerExecution = Mockito.mock(HandlerExecution.class);

        assertThat(HANDLER_ADAPTER.supports(handlerExecution)).isTrue();
    }

    @DisplayName("handle 할 수 있는 handler인지 반환한다 (false)")
    @Test
    void supports_False() {
        Controller controller = Mockito.mock(Controller.class);

        assertThat(HANDLER_ADAPTER.supports(controller)).isFalse();
    }
}
