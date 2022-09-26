package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("특정 핸들러를 처리할 수 있는 어댑터를 추가하고 해당 핸들러를 통해 어댑터를 반환한다.")
    void addHandlerAdapter() {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final HandlerAdapter adapter = mock(HandlerAdapter.class);
        final Object handler = new Object();
        given(adapter.supports(handler)).willReturn(true);

        handlerAdapterRegistry.addHandlerAdapter(adapter);

        // when
        final HandlerAdapter expected = handlerAdapterRegistry.getAdapter(handler);

        // then
        assertAll(
                () -> verify(adapter).supports(handler),
                () -> assertThat(expected).isEqualTo(adapter)
        );
    }
}
