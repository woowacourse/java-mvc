package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletInitializerTest {

    @Test
    void registersMvcDispatcherForApplicationRoutes() {
        final var context = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(eq("dispatcher"), any(Servlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(context);

        final var servlet = ArgumentCaptor.forClass(Servlet.class);
        verify(context).addServlet(eq("dispatcher"), servlet.capture());
        assertThat(servlet.getValue()).isInstanceOf(DispatcherServlet.class);
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
    }

    @Test
    void rejectsDuplicateServletRegistration() {
        assertThatThrownBy(() -> new DispatcherServletInitializer().onStartup(mock(ServletContext.class)))
                .isInstanceOf(IllegalStateException.class);
    }
}
