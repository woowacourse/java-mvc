package com.techcourse;

import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletInitializerTest {

    private ServletContext servletContext;
    private ServletRegistration.Dynamic registration;
    private DispatcherServletInitializer initializer;

    @BeforeEach
    void setUp() {
        servletContext = mock(ServletContext.class);
        registration = mock(ServletRegistration.Dynamic.class);
        initializer = new DispatcherServletInitializer();
    }

    @Test
    void DispatcherServlet을_dispatcher라는_이름으로_등록한다() {
        when(servletContext.addServlet(anyString(), any(Servlet.class))).thenReturn(registration);

        initializer.onStartup(servletContext);

        verify(servletContext).addServlet(eq("dispatcher"), isA(DispatcherServlet.class));
    }

    @Test
    void 모든_요청을_받도록_루트_경로에_매핑한다() {
        when(servletContext.addServlet(anyString(), any(Servlet.class))).thenReturn(registration);

        initializer.onStartup(servletContext);

        verify(registration).addMapping("/");
    }

    @Test
    void 서버가_시작될_때_서블릿을_바로_초기화하도록_설정한다() {
        when(servletContext.addServlet(anyString(), any(Servlet.class))).thenReturn(registration);

        initializer.onStartup(servletContext);

        verify(registration).setLoadOnStartup(1);
    }

    @Test
    void 같은_이름의_서블릿이_이미_등록되어_있으면_예외가_발생한다() {
        when(servletContext.addServlet(anyString(), any(Servlet.class))).thenReturn(null);

        assertThatThrownBy(() -> initializer.onStartup(servletContext))
                .isInstanceOf(IllegalStateException.class);
    }
}
