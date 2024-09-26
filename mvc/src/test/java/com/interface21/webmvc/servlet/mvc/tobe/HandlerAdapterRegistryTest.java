package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("올바른 핸들러 어댑터를 반환한다.")
    void getHandlerAdapter() {
        Object handler = mock(Object.class);

        HandlerAdapter firstAdapter = mock(HandlerAdapter.class);
        HandlerAdapter secondAdapter = mock(HandlerAdapter.class);
        HandlerAdapter thirdAdapter = mock(HandlerAdapter.class);

        when(firstAdapter.supports(handler)).thenReturn(false);
        when(secondAdapter.supports(handler)).thenReturn(true);
        when(thirdAdapter.supports(handler)).thenReturn(false);

        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(firstAdapter);
        registry.addHandlerAdapter(secondAdapter);
        registry.addHandlerAdapter(thirdAdapter);

        HandlerAdapter result = registry.getHandlerAdapter(handler);

        assertThat(result).isEqualTo(secondAdapter);
    }

    @Test
    @DisplayName("해당 핸들러를 지원하는 어댑터가 없으면 예외를 반환한다.")
    void getHandlerAdapter_throwsException_WhenNoAdapterSupportsHandler() {
        Object handler = mock(Object.class);

        HandlerAdapter firstAdapter = mock(HandlerAdapter.class);
        HandlerAdapter secondAdapter = mock(HandlerAdapter.class);

        when(firstAdapter.supports(handler)).thenReturn(false);
        when(secondAdapter.supports(handler)).thenReturn(false);

        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(firstAdapter);
        registry.addHandlerAdapter(secondAdapter);

        assertThatThrownBy(() -> registry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
