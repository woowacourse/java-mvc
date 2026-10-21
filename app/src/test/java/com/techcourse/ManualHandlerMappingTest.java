package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private HandlerMapping mapping;

    @BeforeEach
    void setUp() {
        mapping = new ManualHandlerMapping();
        mapping.initialize();
    }

    @Test
    void findsLegacyControllerFromRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        final var controller = (Controller) mapping.getHandler(request);

        assertThat(controller.execute(request, mock(HttpServletResponse.class))).isEqualTo("/index.jsp");
    }

    @Test
    void returnsNullWhenPathIsNotRegistered() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/missing");

        assertThat(mapping.getHandler(request)).isNull();
    }
}
