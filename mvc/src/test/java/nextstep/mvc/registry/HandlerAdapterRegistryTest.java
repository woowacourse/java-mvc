package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nextstep.mvc.adapter.HandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = mock(HandlerAdapter.class);
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @DisplayName("handlerAdapter를 레지스트리에 추가한다.")
    @Test
    void addHandlerAdapter() {
        assertThatCode(() -> handlerAdapterRegistry.addHandlerAdapter(handlerAdapter))
            .doesNotThrowAnyException();
    }

    @DisplayName("적절한 HandlerAdapter를 찾아 리턴한다. - 성공")
    @Test
    void findHandlerAdapter() {
        // given
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
        Object handler = new Object();

        // when
        when(handlerAdapter.supports(handler)).thenReturn(Boolean.TRUE);

        // then
        assertThat(handlerAdapterRegistry.findHandlerAdapter(handler))
            .isNotNull();
        assertThat(handlerAdapterRegistry.findHandlerAdapter(handler))
            .isInstanceOf(HandlerAdapter.class);
    }

    @DisplayName("적절한 HandlerAdapter를 찾아 리턴한다. - 실패, 지원하는 HandlerAdapter 없음.")
    @Test
    void findHandlerAdapterFailed() {
        // given
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
        Object invalidHandler = new Object();

        // when
        when(handlerAdapter.supports(invalidHandler)).thenReturn(Boolean.FALSE);

        // then
        assertThatThrownBy(() -> handlerAdapterRegistry.findHandlerAdapter(invalidHandler))
            .isInstanceOf(IllegalStateException.class);

    }
}
