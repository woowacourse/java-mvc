package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControllerHandlerMappingTest {

    private ControllerHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ControllerHandlerMapping("index.jsp", "samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/test/controller/interface/view");
        when(request.getMethod()).thenReturn("GET");

        final var controller = (Controller) handlerMapping.getHandler(request);
        final var viewName = controller.execute(request, response);

        assertThat(viewName).isEqualTo("controller based handler get");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/test/controller/interface");
        when(request.getMethod()).thenReturn("POST");

        final var controller = (Controller) handlerMapping.getHandler(request);
        final var viewName = controller.execute(request, response);

        assertThat(viewName).isEqualTo("controller based handler post");
    }
}
