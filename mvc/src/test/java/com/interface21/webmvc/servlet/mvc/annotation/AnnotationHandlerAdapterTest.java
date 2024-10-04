package com.interface21.webmvc.servlet.mvc.annotation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private HandlerAdapter adapters;
    private HandlerMapping handlerMapping;

    @BeforeEach
    void setAdapters() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        adapters = new AnnotationHandlerAdapter();
    }

    @Test
    @DisplayName("어댑터는 주어진 핸들러를 자신이 처리할 수 있는지 검증할 수 있다.")
    void supports() {
        // given
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final var annotatedController = handlerMapping.getHandler(request);

        // then
        assertTrue(adapters.supports(annotatedController));
    }
}
