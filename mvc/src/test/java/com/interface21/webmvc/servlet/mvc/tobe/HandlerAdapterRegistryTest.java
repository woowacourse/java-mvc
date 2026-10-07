package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러를 지원하는 HandlerAdapter를 반환한다")
    void getHandlerAdapter() {
        final var handler = new Object();
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        final var supportedAdapter = mock(HandlerAdapter.class);
        when(unsupportedAdapter.supports(handler)).thenReturn(false);
        when(supportedAdapter.supports(handler)).thenReturn(true);

        final var registry = new HandlerAdapterRegistry(new ArrayList<>());
        registry.addHandlerAdapter(unsupportedAdapter);
        registry.addHandlerAdapter(supportedAdapter);

        assertThat(registry.getHandlerAdapter(handler)).isSameAs(supportedAdapter);
        verify(unsupportedAdapter).supports(handler);
        verify(supportedAdapter).supports(handler);
    }

    @Test
    @DisplayName("핸들러를 지원하는 HandlerAdapter가 없으면 예외를 던진다")
    void throwExceptionWhenAdapterDoesNotExist() {
        final var handler = new Object();
        final var handlerAdapter = mock(HandlerAdapter.class);
        final var registry = new HandlerAdapterRegistry(
                new ArrayList<>(List.of(handlerAdapter))
        );

        assertThatThrownBy(() -> registry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(handler.getClass().getName());
    }
}
