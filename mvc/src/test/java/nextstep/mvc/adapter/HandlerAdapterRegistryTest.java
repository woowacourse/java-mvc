package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @DisplayName("handler에 알맞은 adapter를 찾는다.")
    @Test
    void getAdapter() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.init();

        final HandlerExecution handler = mock(HandlerExecution.class);
        assertThat(handlerAdapterRegistry.getAdapter(handler)).isInstanceOf(HandlerExecutionAdapter.class);
    }
}
