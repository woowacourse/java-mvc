package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @Test
    void 같은_요청을_처리할_수_있으면_먼저_등록한_매핑의_핸들러를_반환한다() {
        final var request = mock(HttpServletRequest.class);
        final var annotationHandlerMapping = mock(HandlerMapping.class);
        final var manualHandlerMapping = mock(HandlerMapping.class);
        final var annotationHandler = new Object();
        when(annotationHandlerMapping.getHandler(request)).thenReturn(annotationHandler);
        when(manualHandlerMapping.getHandler(request)).thenReturn(new Object());
        final var registry = new HandlerMappingRegistry(List.of(annotationHandlerMapping, manualHandlerMapping));

        assertThat(registry.getHandler(request)).isSameAs(annotationHandler);
        verifyNoInteractions(manualHandlerMapping);
    }
}
