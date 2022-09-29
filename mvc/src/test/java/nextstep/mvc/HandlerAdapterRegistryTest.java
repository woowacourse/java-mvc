package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import samples.TestHandlerAdapter;

class HandlerAdapterRegistryTest {

    @Test
    void getHandlerAdapter() {
        TestHandlerAdapter targetAdapter = mock(TestHandlerAdapter.class);
        TestHandlerAdapter otherAdapter = mock(TestHandlerAdapter.class);
        Object handler = mock(Object.class);
        when(targetAdapter.supports(handler)).thenReturn(true);
        when(otherAdapter.supports(handler)).thenReturn(false);
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(targetAdapter);
        handlerAdapterRegistry.addHandlerAdapter(otherAdapter);

        HandlerAdapter result = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(result).isEqualTo(targetAdapter);
    }

}
