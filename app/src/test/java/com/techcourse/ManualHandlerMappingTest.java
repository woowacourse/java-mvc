package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        final var mapping = new ManualHandlerMapping();
        mapping.initialize();
        handlerMapping = mapping;
    }

    @Test
    void findsHandlerByRequestUri() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(ForwardController.class);
    }

    @Test
    void returnsNullForUnmappedRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unmapped");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
