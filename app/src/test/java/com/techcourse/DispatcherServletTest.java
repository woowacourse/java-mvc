package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techcourse.controller.LoginViewController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

@DisplayNameGeneration(ReplaceUnderscores.class)
class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        manualHandlerMapping = mock(ManualHandlerMapping.class);
        annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        dispatcherServlet = new DispatcherServlet(manualHandlerMapping, annotationHandlerMapping);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void 등록된_handler들중에_RequestURI가_중복되는_경우_예외가_발생한다() throws ServletException {
        // given
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        when(manualHandlerMapping.getHandler("/")).thenReturn(any(Controller.class));
        when(annotationHandlerMapping.getHandler(request)).thenReturn(any(HandlerExecution.class));

        // expect
        assertThatThrownBy(() -> dispatcherServlet.service(request, response)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void RequestURI를_처리할_handler가_없으면_404_페이지로_리다이렉트한다() throws IOException, ServletException {
        // given
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        when(manualHandlerMapping.getHandler("/")).thenReturn(null);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(null);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(response, times(1)).sendRedirect("404.jsp");
    }

    @Test
    void Controller만_처리_가능한_경우_해당_Handler가_동작한다() throws Exception {
        // given
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        var loginViewController = mock(LoginViewController.class);
        when(manualHandlerMapping.getHandler("/")).thenReturn(loginViewController);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(null);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(loginViewController, times(1)).execute(request, response);
    }

    @Test
    void HandlerExecution만_처리_가능한_경우_해당_Handler가_동작한다() throws Exception {
        // given
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        var handlerExecution = mock(HandlerExecution.class);
        when(manualHandlerMapping.getHandler("/")).thenReturn(null);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(handlerExecution);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(handlerExecution, times(1)).handle(request, response);
    }
}
