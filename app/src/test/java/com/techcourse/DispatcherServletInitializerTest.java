package com.techcourse;

import com.interface21.web.http.MediaType;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletInitializerTest {

    @Test
    void registersServletThatHandlesAppControllers() throws Exception {
        final var context = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(eq("dispatcher"), any(Servlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(context);

        final var servlet = ArgumentCaptor.forClass(Servlet.class);
        verify(context).addServlet(eq("dispatcher"), servlet.capture());
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        servlet.getValue().init(mock(jakarta.servlet.ServletConfig.class));

        servlet.getValue().service(request, response);

        assertEquals("{\"account\":\"gugu\"}", body.toString());
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
