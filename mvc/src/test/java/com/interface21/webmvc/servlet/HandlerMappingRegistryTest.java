package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerMappingRegistryTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void 핸들러를_찾은_첫_번째_매핑의_결과를_반환한다() {
        final var firstHandler = new Object();
        final var secondHandler = new Object();
        final var registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(request -> null);
        registry.addHandlerMapping(request -> firstHandler);
        registry.addHandlerMapping(request -> secondHandler);

        assertThat(registry.getHandler(request)).containsSame(firstHandler);
    }

    @Test
    void 모든_매핑에서_핸들러를_찾지_못하면_빈_값을_반환한다() {
        final var registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(request -> null);

        assertThat(registry.getHandler(request)).isEmpty();
    }
}
