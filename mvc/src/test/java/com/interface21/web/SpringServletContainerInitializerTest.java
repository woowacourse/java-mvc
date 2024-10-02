package com.interface21.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;

import com.interface21.webmvc.servlet.mvc.tobe.DispatcherServletInitializer;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class SpringServletContainerInitializerTest {

    @Test
    @DisplayName("webAppInitializerClasses 매개변수가 null일 때 패키지 전체를 탐색해 초기화한다.")
    void onStartupWhenClassesIsNull() throws ServletException {
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        Dynamic dynamic = Mockito.mock(Dynamic.class);
        Mockito.when(servletContext.addServlet(anyString(), ArgumentMatchers.any(Servlet.class))).thenReturn(dynamic);

        ArrayList<WebApplicationInitializer> initializers = new ArrayList<>();
        SpringServletContainerInitializer servletContainerInitializer = new SpringServletContainerInitializer(
                initializers);

        servletContainerInitializer.onStartup(null, servletContext);

        assertAll(
                () -> assertThat(initializers).hasSize(1),
                () -> assertThat(initializers.getFirst().getClass()).isEqualTo(DispatcherServletInitializer.class)
        );
    }
}
