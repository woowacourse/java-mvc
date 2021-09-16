package com.techcourse.controller;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static com.techcourse.controller.UserSession.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {
    private AnnotationHandlerMapping handlerMapping;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession httpSession;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        try {
            handlerMapping.initialize();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);

    }

    @Test
    @DisplayName("로그인 성공 시")
    void login() {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(httpSession.getAttribute(SESSION_KEY)).thenReturn(null);

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/index.jsp"));
    }

    @Test
    @DisplayName("로그인 실패 시")
    void loginFail() {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("pass");
        when(httpSession.getAttribute(SESSION_KEY)).thenReturn(null);

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/401.jsp"));
    }

    @Test
    @DisplayName("세션 존재 시")
    void loginSession() {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(SESSION_KEY)).thenReturn(InMemoryUserRepository.findByAccount("gugu").get());

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/index.jsp"));
    }

    @Test
    @DisplayName("로그인 페이지")
    void loginView() {
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("/login.jsp"));
    }

    @Test
    @DisplayName("로그인 페이지 세션")
    void loginViewWithSession() {
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(SESSION_KEY)).thenReturn(InMemoryUserRepository.findByAccount("gugu").get());

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/index.jsp"));
    }
}
