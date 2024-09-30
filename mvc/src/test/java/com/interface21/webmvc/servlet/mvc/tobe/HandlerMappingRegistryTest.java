package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("요청을 처리할 수 있는 핸들러 결과를 반환한다.")
    void return_handler() throws Exception {
        // given
        final var handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("lemon");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");

        // when
        final Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("요청을 처리할 수 있는 핸들러가 없으면 예외가 발생한다.")
    void throw_exception_when_not_handle() throws Exception {
        // given
        final var handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/not-exist");

        // when & then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("request에 맞는 Handler가 존재하지 않습니다.");
    }
}
