package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletInitializerTest {

    @Test
    @DisplayName("디스패처 서블릿을 루트 경로에 등록하고 시작 시 로드한다")
    void register_dispatcher_servlet() {
        final var servletContext = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(eq("dispatcher"), any(DispatcherServlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(servletContext);

        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
    }

    @Test
    @DisplayName("서블릿을 등록하지 못하면 등록 이름을 포함한 예외를 전달한다")
    void fail_when_dispatcher_registration_is_unavailable() {
        final var servletContext = mock(ServletContext.class);

        assertThatThrownBy(() -> new DispatcherServletInitializer().onStartup(servletContext))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("dispatcher");
    }
}
