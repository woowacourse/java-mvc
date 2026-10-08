package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 지원하는_어댑터가_없으면_예외가_발생한다() {
        // given
        HandlerAdapterRegistry registry = HandlerAdapterRegistry.empty();
        registry.addHandlerAdapter(new HandlerExecutionAdapter());

        // when & then
        assertThatThrownBy(() -> registry.getHandlerAdapter(new Object()))
            .isInstanceOf(HandlerAdapterNotFoundException.class);
    }

    @Test
    void 핸들러를_지원하는_어댑터를_선택한다() {
        // given
        Object handler = new Object();
        HandlerAdapter unsupported = mock(HandlerAdapter.class);
        HandlerAdapter supported = mock(HandlerAdapter.class);

        when(unsupported.isSupported(handler)).thenReturn(false);
        when(supported.isSupported(handler)).thenReturn(true);

        HandlerAdapterRegistry registry = HandlerAdapterRegistry.empty();
        registry.addHandlerAdapter(unsupported);
        registry.addHandlerAdapter(supported);

        // when
        HandlerAdapter actual = registry.getHandlerAdapter(handler);

        // then
        assertThat(actual).isSameAs(supported);
    }

}