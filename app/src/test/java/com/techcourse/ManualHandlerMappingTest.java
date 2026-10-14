package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.techcourse.controller.LoginViewController;
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
        when(request.getRequestURI()).thenReturn("/login/view");

        assertThat(handlerMapping.getHandler(request)).isInstanceOf(LoginViewController.class);
    }

    @Test
    void returnsNullForUnknownUri() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unknown");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
