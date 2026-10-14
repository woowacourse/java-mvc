package com.techcourse;

import com.techcourse.controller.UserSession;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void 기존_컨트롤러의_JSP_응답을_forward한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 기존_컨트롤러의_redirect_응답을_유지한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 같은_주소가_겹치면_어노테이션_매핑을_먼저_사용한다() throws Exception {
        // given
        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        ManualHandlerMapping manualHandlerMapping = mock(ManualHandlerMapping.class);
        DispatcherServlet servlet = new DispatcherServlet(annotationHandlerMapping, manualHandlerMapping);
        servlet.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HandlerExecution execution = mock(HandlerExecution.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(dispatcher);
        when(annotationHandlerMapping.getHandler(request)).thenReturn(execution);
        when(execution.handle(request, response)).thenReturn(new ModelAndView(new JspView("/annotation.jsp")));

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(manualHandlerMapping, never()).getHandler(request);
    }
}
