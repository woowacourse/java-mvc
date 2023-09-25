package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoginControllerTest {

    @Test
    void 로그인_성공_테스트() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        LoginController loginController = new LoginController();
        ModelAndView login = loginController.login(request, response);

        Assertions.assertThat(login.getView()).isEqualTo(JspView.redirect("/index.jsp"));
    }

    @Test
    void 로그인_실패_테스트() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("account")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        LoginController loginController = new LoginController();
        ModelAndView login = loginController.login(request, response);

        Assertions.assertThat(login.getView()).isEqualTo(JspView.redirect("/401.jsp"));
    }

    @Test
    void 세션_존재_할때_로그인_테스트() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(session.getAttribute("user")).thenReturn(new User("power", "power", "power@naver.com"));
        when(request.getSession()).thenReturn(session);

        LoginController loginController = new LoginController();
        ModelAndView login = loginController.login(request, response);

        Assertions.assertThat(login.getView()).isEqualTo(JspView.redirect("/index.jsp"));
    }
}
