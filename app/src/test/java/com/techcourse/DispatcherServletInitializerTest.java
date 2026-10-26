package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletInitializerTest {

    @Test
    void 앱_컨트롤러를_찾는_서블릿을_등록한다() throws Exception {
        // given
        ServletContext servletContext = mock(ServletContext.class);
        ServletRegistration.Dynamic registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(eq("dispatcher"), any(DispatcherServlet.class)))
                .thenReturn(registration);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        new DispatcherServletInitializer().onStartup(servletContext);
        ArgumentCaptor<DispatcherServlet> servletCaptor = ArgumentCaptor.forClass(DispatcherServlet.class);
        verify(servletContext).addServlet(eq("dispatcher"), servletCaptor.capture());
        DispatcherServlet servlet = servletCaptor.getValue();
        servlet.init();
        servlet.service((ServletRequest) request, (ServletResponse) response);

        // then
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
        verify(dispatcher).forward(request, response);
    }
}
