package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.RegisterViewController;
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
    void findsManuallyRegisteredControllersByRequestUri() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/");
        assertThat(handlerMapping.getHandler(request)).isInstanceOf(ForwardController.class);

        when(request.getRequestURI()).thenReturn("/login");
        assertThat(handlerMapping.getHandler(request)).isInstanceOf(LoginController.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        assertThat(handlerMapping.getHandler(request)).isInstanceOf(RegisterViewController.class);

        when(request.getRequestURI()).thenReturn("/register");
        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @Test
    void returnsNullForUnmappedRequest() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/missing");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }
}
