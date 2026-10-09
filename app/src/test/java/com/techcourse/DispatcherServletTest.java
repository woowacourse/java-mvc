package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() throws ServletException {
        dispatcherServlet = registerDispatcherServlet();
    }

    private DispatcherServlet registerDispatcherServlet() throws ServletException {
        final var servletContext = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(eq("dispatcher"), any(DispatcherServlet.class)))
                .thenReturn(registration);

        new DispatcherServletInitializer().onStartup(servletContext);

        final var dispatcherServletCaptor = ArgumentCaptor.forClass(DispatcherServlet.class);
        verify(servletContext).addServlet(eq("dispatcher"), dispatcherServletCaptor.capture());
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");

        final var dispatcherServlet = dispatcherServletCaptor.getValue();
        dispatcherServlet.init(mock(ServletConfig.class));
        return dispatcherServlet;
    }

    @Test
    void givenLegacyControllerRequest_whenServices_thenRendersView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenAnnotationControllerRequest_whenServices_thenRendersView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp"))
                .thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenUserRequest_whenServices_thenRendersJsonResponse() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var responseBody = new StringWriter();

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        dispatcherServlet.service(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(responseBody.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void givenBothMappingsHandleSameUri_whenServices_thenUsesAnnotationHandlerFirst() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var annotationRequestDispatcher = mock(RequestDispatcher.class);
        final var legacyRequestDispatcher = mock(RequestDispatcher.class);
        final var handlerExecution = mock(HandlerExecution.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(annotationRequestDispatcher);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(legacyRequestDispatcher);
        when(handlerExecution.handle(request, response))
                .thenReturn(new ModelAndView(new JspView("/annotation.jsp")));

        try (var ignored = mockConstruction(
                AnnotationHandlerMapping.class,
                (mapping, context) -> when(mapping.getHandler(request)).thenReturn(handlerExecution)
        )) {
            registerDispatcherServlet().service(request, response);
        }

        verify(annotationRequestDispatcher).forward(request, response);
        verify(legacyRequestDispatcher, never()).forward(request, response);
    }

    @Test
    void givenNoMatchingHandler_whenServices_thenSendsNotFoundAndStops() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handlerMapping = (HandlerMapping) ignored -> null;
        final var handlerAdapter = mock(HandlerAdapter.class);
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping), List.of(handlerAdapter));

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
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping), List.of(handlerAdapter));

        when(handlerAdapter.supports(handler)).thenReturn(false);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .satisfies(exception -> {
                    assertThat(exception.getCause()).isInstanceOf(IllegalStateException.class);
                    assertThat(exception.getCause().getMessage()).contains(Object.class.getName());
                });

        verify(handlerAdapter, never()).handle(request, response, handler);
    }

    @Test
    void givenHandlerExecutionFails_whenServices_thenPreservesCause() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = new Object();
        final var handlerMapping = (HandlerMapping) ignored -> handler;
        final var handlerAdapter = mock(HandlerAdapter.class);
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping), List.of(handlerAdapter));
        final var failure = new IllegalArgumentException("handler failed");

        when(handlerAdapter.supports(handler)).thenReturn(true);
        when(handlerAdapter.handle(request, response, handler)).thenThrow(failure);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(failure);
    }
}
