package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void givenHandlerRequest_whenServices_thenRendersView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        final var handler = new Object();
        final var handlerMapping = (HandlerMapping) ignored -> handler;
        final var handlerAdapter = mock(HandlerAdapter.class);

        when(handlerAdapter.supports(handler)).thenReturn(true);
        when(handlerAdapter.handle(request, response, handler))
                .thenReturn(new ModelAndView(new JspView("/index.jsp")));

        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        final var dispatcherServlet = new DispatcherServlet(
                List.of(handlerMapping),
                List.of(handlerAdapter)
        );

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenNoMatchingHandler_whenServices_thenSendsNotFoundAndStops() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var handlerMapping = (HandlerMapping) ignored -> null;
        final var handlerAdapter = mock(HandlerAdapter.class);

        final var dispatcherServlet = new DispatcherServlet(
                List.of(handlerMapping),
                List.of(handlerAdapter)
        );

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(handlerAdapter);
    }

    @Test
    void givenHandlerWithoutSupportingAdapter_whenServices_thenReportsHandlerTypeAndStops() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var handler = new Object();
        final var handlerMapping = (HandlerMapping) ignored -> handler;
        final var handlerAdapter = mock(HandlerAdapter.class);

        when(handlerAdapter.supports(handler)).thenReturn(false);

        final var dispatcherServlet = new DispatcherServlet(
                List.of(handlerMapping),
                List.of(handlerAdapter)
        );

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .satisfies(exception -> {
                    assertThat(exception.getCause())
                            .isInstanceOf(IllegalStateException.class);

                    assertThat(exception.getCause().getMessage())
                            .contains(Object.class.getName());
                });

        verify(handlerAdapter, never())
                .handle(request, response, handler);
    }

    @Test
    void givenHandlerExecutionFails_whenServices_thenPreservesCause() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var handler = new Object();
        final var handlerMapping = (HandlerMapping) ignored -> handler;
        final var handlerAdapter = mock(HandlerAdapter.class);

        final var failure = new IllegalArgumentException("handler failed");

        when(handlerAdapter.supports(handler)).thenReturn(true);
        when(handlerAdapter.handle(request, response, handler))
                .thenThrow(failure);

        final var dispatcherServlet = new DispatcherServlet(
                List.of(handlerMapping),
                List.of(handlerAdapter)
        );

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(failure);
    }
}
