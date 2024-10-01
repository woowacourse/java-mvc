package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HandlerMappingRegistryTest {
    private static final String BASE_PACKAGE = "samples";
    private static final String REQUEST_URI_GET_TEST = "/get-test";
    private static final String METHOD_GET = "GET";

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setup() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @DisplayName("AnnotationHandlerMapping(@MVC)이 요청을 처리할 수 있다.")
    @Test
    void test_GetHandler_When_Use_AnnotationHandlerMapping() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        registerHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE));

        // when
        final Object handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("요청을 처리할 수 있는 핸들러가 없으면, IllegalArgumentException이 발생한다.")
    @Test
    void test_GetHandler_ReturnNull_When_NoHandler() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        // when & then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request URI를 처리할 핸들러가 없습니다.");
    }

    private void registerHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }
}
