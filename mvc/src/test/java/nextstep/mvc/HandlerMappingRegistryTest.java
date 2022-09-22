package nextstep.mvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("Registry에서 Handler(HandlerExecution)를 조회한다.")
    void getHandlerExecution() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final Object handler = handlerMappingRegistry.getHandler(request).orElseThrow();

        // then
        assertThat(handler.getClass().getSimpleName()).isEqualTo("HandlerExecution");
    }

    @Test
    @DisplayName("Registry에서 존재하지 않는 handler를 조회하면 예외를 반환한다.")
    void getHandlerExecution_exception() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        // when, then
        assertThat(handlerMappingRegistry.getHandler(request)).isEmpty();
    }
}
