package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingsTest {

    private static final HttpServletRequest VALID_REQUEST = mock(HttpServletRequest.class);
    private static final HttpServletRequest INVALID_REQUEST = mock(HttpServletRequest.class);
    private static final HandlerMappings handlerMappings = new HandlerMappings();
    private static final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");

    @BeforeAll
    static void setUp() {
        when(VALID_REQUEST.getAttribute("id")).thenReturn("gugu");
        when(VALID_REQUEST.getMethod()).thenReturn("GET");
        when(VALID_REQUEST.getRequestURI()).thenReturn("/get-test");

        when(INVALID_REQUEST.getAttribute("id")).thenReturn("gugu");
        when(INVALID_REQUEST.getMethod()).thenReturn("POST");
        when(INVALID_REQUEST.getRequestURI()).thenReturn("/invalid-uri");

        handlerMapping.initialize();
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    @Test
    @DisplayName("Request에 대해 올바른 handler를 반환한다.")
    void getHandler() {
        assertThat(handlerMappings.getHandler(VALID_REQUEST))
                .isEqualTo(handlerMapping.getHandler(VALID_REQUEST));
    }

    @Test
    @DisplayName("요청을 처리할 handler가 없는 경우 null을 반환한다.")
    void getHandler_returnNull_NoHandler() {
        assertThat(handlerMappings.getHandler(INVALID_REQUEST)).isNull();
    }
}
