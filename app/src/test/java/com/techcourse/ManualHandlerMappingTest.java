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
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMapping = manualHandlerMapping;
    }

    @Test
    void findsLegacyControllerByRequestUri() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(ForwardController.class);
    }

    @Test
    void returnsNullForUnknownUri() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void doesNotRegisterMigratedController() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
