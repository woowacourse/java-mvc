package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @Test
    @DisplayName("요청에 대한 올바른 핸들러를 반환한다.")
    void getHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");

        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        when(handlerMapping.getHandler(request)).thenReturn(new AnnotationHandlerMapping());

        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(AnnotationHandlerMapping.class);
    }
}
