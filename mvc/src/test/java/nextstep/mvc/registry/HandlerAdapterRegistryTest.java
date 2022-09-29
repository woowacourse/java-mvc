package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.HandlerExecutionHandlerAdapter;
import nextstep.mvc.exception.HandlerAdapterNotFoundException;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 어댑터를_레지스트리에_추가한다() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(handlerExecutionHandlerAdapter);

        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isEqualTo(handlerExecutionHandlerAdapter);
    }

    @Test
    void 처리할_수_있는_어댑터가_없으면_예외를_반환한다() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(HandlerAdapterNotFoundException.class);
    }
}
