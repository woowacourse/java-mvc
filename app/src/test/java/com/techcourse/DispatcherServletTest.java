package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void rendersViewReturnedByController() throws Exception {
        final var handlerMapping = mock(ManualHandlerMapping.class);
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var dispatcherServlet = new DispatcherServlet(handlerMapping);

        when(request.getRequestURI()).thenReturn("/test");
        when(handlerMapping.getHandler(request)).thenReturn(controller);
        when(controller.execute(request, response)).thenReturn("/test.jsp");
        when(request.getRequestDispatcher("/test.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
