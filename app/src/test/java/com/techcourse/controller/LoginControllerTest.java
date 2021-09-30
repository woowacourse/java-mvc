package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginControllerTest extends ControllerTest {

    @DisplayName("login을 한다. - 성공")
    @Test
    void login() {
        // given
        LoginController loginController = new LoginController();

        // when
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        final ModelAndView modelAndView = loginController.login(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("redirect:/");
    }

    @DisplayName("login을 한다. - 실패, 존재하지 않는 유저")
    @Test
    void loginFailed() {
        // given
        LoginController loginController = new LoginController();

        // when
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("solong");
        when(request.getParameter("password")).thenReturn("password");

        final ModelAndView modelAndView = loginController.login(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("redirect:/401.jsp");
    }

    @DisplayName("login 페이지를 보여준다.")
    @Test
    void show() {
        // given
        LoginController loginController = new LoginController();

        // when
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final ModelAndView modelAndView = loginController.show(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("/login.jsp");
    }

    @DisplayName("logout을 한다.")
    @Test
    void logout() {
        // given
        LoginController loginController = new LoginController();

        // when
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final ModelAndView modelAndView = loginController.logout(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("redirect:/");
    }
}
