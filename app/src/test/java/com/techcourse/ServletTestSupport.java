package com.techcourse;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ServletTestSupport {

    private ServletTestSupport() {
    }

    static Servlet createServlet() {
        final var context = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        final var servlet = ArgumentCaptor.forClass(Servlet.class);
        when(context.addServlet(eq("dispatcher"), servlet.capture())).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(context);

        return servlet.getValue();
    }
}
