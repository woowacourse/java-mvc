package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
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
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping),
                List.of(new HandlerExecutionAdapter(), new ControllerHandlerAdapter()));

        when(request.getRequestURI()).thenReturn("/test");
        when(handlerMapping.getHandler(request)).thenReturn(controller);
        when(controller.execute(request, response)).thenReturn("/test.jsp");
        when(request.getRequestDispatcher("/test.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void usesNextMappingAndRendersAnnotationHandlerModel() throws Exception {
        final var firstMapping = mock(HandlerMapping.class);
        final var secondMapping = mock(HandlerMapping.class);
        final var handler = mock(HandlerExecution.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = mock(View.class);
        final var result = new ModelAndView(view).addObject("id", "gugu");
        when(secondMapping.getHandler(request)).thenReturn(handler);
        when(handler.handle(request, response)).thenReturn(result);
        final var servlet = new DispatcherServlet(List.of(firstMapping, secondMapping),
                List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter()));

        servlet.service(request, response);

        verify(firstMapping).getHandler(request);
        verify(handler).handle(request, response);
        verify(view).render(result.getModel(), request, response);
    }

    @Test
    void selectsFirstMatchingMapping() throws Exception {
        final var firstMapping = mock(HandlerMapping.class);
        final var secondMapping = mock(HandlerMapping.class);
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(firstMapping.getHandler(request)).thenReturn(controller);
        when(controller.execute(request, response)).thenReturn("redirect:/");
        final var servlet = new DispatcherServlet(List.of(firstMapping, secondMapping),
                List.of(new ControllerHandlerAdapter()));

        servlet.service(request, response);

        verify(response).sendRedirect("/");
        verifyNoInteractions(secondMapping);
    }

    @Test
    void sendsNotFoundWhenNoHandlerMatches() throws Exception {
        final var mapping = mock(HandlerMapping.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var servlet = new DispatcherServlet(List.of(mapping), List.of());

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void failsWhenNoAdapterSupportsHandler() {
        final var mapping = mock(HandlerMapping.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(mapping.getHandler(request)).thenReturn(new Object());
        final var servlet = new DispatcherServlet(List.of(mapping),
                List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter()));

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No adapter for handler");
    }

    @Test
    void initializesDefaultMappingsAndServesLegacyHome() throws Exception {
        final var servlet = new DispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.init();
        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }
}
