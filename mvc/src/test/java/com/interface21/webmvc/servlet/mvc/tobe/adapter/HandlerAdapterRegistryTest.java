package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HandlerAdapterRegistry 테스트")
class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerAdapter mockAdapter1;
    private HandlerAdapter mockAdapter2;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        mockAdapter1 = mock(HandlerAdapter.class);
        mockAdapter2 = mock(HandlerAdapter.class);
    }

    @Test
    @DisplayName("지원하는 Adapter 반환 테스트")
    void returnCorrectAdapter() {
        Object handler = new Object();
        when(mockAdapter1.supports(handler)).thenReturn(false);
        when(mockAdapter2.supports(handler)).thenReturn(true);

        handlerAdapterRegistry.addHandlerAdapter(mockAdapter1);
        handlerAdapterRegistry.addHandlerAdapter(mockAdapter2);

        HandlerAdapter result = handlerAdapterRegistry.getHandlerAdapter(handler);
        assertThat(result).isEqualTo(mockAdapter2);
    }

    @Test
    @DisplayName("지원하는 Adapter가 없을 때 예외 발생 테스트")
    void throwExceptionIfNoAdapter() {
        Object handler = new Object();

        when(mockAdapter1.supports(handler)).thenReturn(false);
        when(mockAdapter2.supports(handler)).thenReturn(false);

        handlerAdapterRegistry.addHandlerAdapter(mockAdapter1);
        handlerAdapterRegistry.addHandlerAdapter(mockAdapter2);

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("핸들러를 처리할 수 있는 Adapter를 찾지 못했습니다");
    }
}
