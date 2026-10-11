package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

class DispatcherServletInitializerTest {

    @Test
    void registersDispatcherWithApplicationControllers() throws Exception {
        final var context = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(eq("dispatcher"), any(DispatcherServlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(context);

        final var captor = ArgumentCaptor.forClass(DispatcherServlet.class);
        verify(context).addServlet(eq("dispatcher"), captor.capture());
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
        final HttpServlet servlet = captor.getValue();
        servlet.init();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }
}
