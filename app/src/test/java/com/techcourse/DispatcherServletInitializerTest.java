package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletInitializerTest {

    @Test
    void registeredServletHandlesRequestsWithInitializedRegistries() throws Exception {
        ServletContext context = mock(ServletContext.class);
        ServletRegistration.Dynamic registration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(eq("dispatcher"), any(Servlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(context);

        ArgumentCaptor<Servlet> servletCaptor = ArgumentCaptor.forClass(Servlet.class);
        verify(context).addServlet(eq("dispatcher"), servletCaptor.capture());
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
        Servlet servlet = servletCaptor.getValue();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher view = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(view);

        servlet.service(request, response);

        verify(view).forward(request, response);

        when(request.getRequestURI()).thenReturn("/missing");
        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void rejectsDuplicateServletRegistration() {
        ServletContext context = mock(ServletContext.class);

        assertThatThrownBy(() -> new DispatcherServletInitializer().onStartup(context))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to register servlet with name 'dispatcher'");
    }
}
