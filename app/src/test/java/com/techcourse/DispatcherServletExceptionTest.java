package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletExceptionTest {

    private DispatcherServlet servlet;
    private HandlerMappingRegistry mappingRegistry;
    private HandlerAdapterRegistry adapterRegistry;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object handler;
    private HandlerAdapter adapter;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handler = new Object();
        adapter = mock(HandlerAdapter.class);

        try (final var mappings = mockConstruction(HandlerMappingRegistry.class);
             final var adapters = mockConstruction(HandlerAdapterRegistry.class)) {
            servlet = new DispatcherServlet();
            mappingRegistry = mappings.constructed().getFirst();
            adapterRegistry = adapters.constructed().getFirst();
        }

        when(mappingRegistry.getHandler(request)).thenReturn(Optional.of(handler));
        when(adapterRegistry.getHandlerAdapter(handler)).thenReturn(adapter);
    }

    @Test
    void preservesIOExceptionFromHandler() throws Exception {
        final var expected = new IOException("handler failed");
        when(adapter.handle(request, response, handler)).thenThrow(expected);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void preservesServletExceptionFromHandler() throws Exception {
        final var expected = new ServletException("handler failed");
        when(adapter.handle(request, response, handler)).thenThrow(expected);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void preservesIOExceptionFromForward() throws Exception {
        final var expected = new IOException("forward failed");
        final var dispatcher = mock(RequestDispatcher.class);
        when(adapter.handle(request, response, handler)).thenReturn(new ModelAndView(new JspView("/index.jsp")));
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        doThrow(expected).when(dispatcher).forward(request, response);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void preservesServletExceptionFromForward() throws Exception {
        final var expected = new ServletException("forward failed");
        final var dispatcher = mock(RequestDispatcher.class);
        when(adapter.handle(request, response, handler)).thenReturn(new ModelAndView(new JspView("/index.jsp")));
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        doThrow(expected).when(dispatcher).forward(request, response);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void preservesIOExceptionFromRedirect() throws Exception {
        final var expected = new IOException("redirect failed");
        when(adapter.handle(request, response, handler)).thenReturn(new ModelAndView(new JspView("redirect:/index.jsp")));
        doThrow(expected).when(response).sendRedirect("/index.jsp");

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void wrapsOtherHandlerExceptionsOnce() throws Exception {
        final var expected = new Exception("handler failed");
        when(adapter.handle(request, response, handler)).thenThrow(expected);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(expected);
    }

    @Test
    void preservesIOExceptionFromAnnotationHandler() throws Exception {
        final var expected = new IOException("annotation handler failed");
        useAnnotationHandler(expected);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void preservesServletExceptionFromAnnotationHandler() throws Exception {
        final var expected = new ServletException("annotation handler failed");
        useAnnotationHandler(expected);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    @Test
    void wrapsMissingAdapterFailure() {
        final var expected = new IllegalArgumentException("adapter not found");
        when(adapterRegistry.getHandlerAdapter(handler)).thenThrow(expected);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(expected);
    }

    @Test
    void preservesIOExceptionWhenSendingNotFound() throws Exception {
        final var expected = new IOException("response failed");
        when(mappingRegistry.getHandler(request)).thenReturn(Optional.empty());
        doThrow(expected).when(response).sendError(HttpServletResponse.SC_NOT_FOUND);

        assertThatThrownBy(() -> servlet.service(request, response)).isSameAs(expected);
    }

    private void useAnnotationHandler(final Exception exception) throws Exception {
        final var controller = new ThrowingController(exception);
        final var method = ThrowingController.class.getMethod("handle", HttpServletRequest.class, HttpServletResponse.class);
        final var execution = new HandlerExecution(controller, method);
        when(mappingRegistry.getHandler(request)).thenReturn(Optional.of(execution));
        when(adapterRegistry.getHandlerAdapter(execution)).thenReturn(new AnnotationHandlerAdapter());
    }

    public static class ThrowingController {

        private final Exception exception;

        public ThrowingController(final Exception exception) {
            this.exception = exception;
        }

        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            throw exception;
        }
    }
}
