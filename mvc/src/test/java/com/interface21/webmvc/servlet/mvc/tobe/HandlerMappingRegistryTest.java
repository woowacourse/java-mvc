package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;
import samples.TestManualHandlerMapping;

public class HandlerMappingRegistryTest {
    private static final String BASE_PACKAGE = "samples";
    private static final String REQUEST_URI_GET_TEST = "/get-test";
    private static final String METHOD_GET = "GET";

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setup() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @DisplayName("ManualHandlerMapping(Legacy MVC)이 요청을 처리할 수 있다.")
    @Test
    void test_GetHandler_When_Use_ManualHandlerMapping() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);

        registerHandlerMapping(new TestManualHandlerMapping());

        // when
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(TestController.class);
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
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isPresent();
        assertThat(handler.get()).isInstanceOf(TestController.class);
    }

    @DisplayName("요청을 처리할 수 있는 핸들러가 없으면, null을 반환한다.")
    @Test
    void test_GetHandler_ReturnNull_When_NoHandler() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(REQUEST_URI_GET_TEST);
        when(request.getMethod()).thenReturn(METHOD_GET);

        // when
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isEmpty();
    }

    private void registerHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }
}
