package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationLoginControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
    }

    @Test
    void getLoginPage() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        JspView view = (JspView) modelAndView.getView();
        assertThat(view.getViewName()).isEqualTo("/login.jsp");
    }

    @Test
    void getLoginPageWhenLoggedIn() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User(1L, "account", "password", "email@email.com"));

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        JspView view = (JspView) modelAndView.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/index.jsp");
    }

    @Test
    void loginSuccess() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        JspView view = (JspView) modelAndView.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/index.jsp");
    }

    @Test
    void loginFailure() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("notPassword");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        JspView view = (JspView) modelAndView.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/401.jsp");
    }

    @Test
    void loginWhenLoggedIn() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User(1L, "account", "password", "email@email.com"));

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        JspView view = (JspView) modelAndView.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/index.jsp");
    }
}
