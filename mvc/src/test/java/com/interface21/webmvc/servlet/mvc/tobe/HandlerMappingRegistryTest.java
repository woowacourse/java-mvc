package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @Test
    void 등록된_순서대로_물어보고_처음_찾은_핸들러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        final var first = mock(HandlerMapping.class);
        final var second = mock(HandlerMapping.class);
        final var handler = new Object();
        when(first.getHandler(request)).thenReturn(null);
        when(second.getHandler(request)).thenReturn(handler);

        final var registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(first);
        registry.addHandlerMapping(second);

        assertThat(registry.getHandler(request)).isSameAs(handler);
    }

    @Test
    void 모든_HandlerMapping이_핸들러를_찾지_못하면_HandlerNotFoundException을_던진다() {
        final var request = mock(HttpServletRequest.class);
        final var handlerMapping = mock(HandlerMapping.class);
        when(handlerMapping.getHandler(request)).thenReturn(null);

        final var registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(handlerMapping);

        assertThatThrownBy(() -> registry.getHandler(request))
                .isInstanceOf(HandlerNotFoundException.class);
    }
}
