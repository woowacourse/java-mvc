package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterControllerTest extends ControllerTest {

    @DisplayName("회원 가입을 한다. - 성공")
    @Test
    void save() {
        // given
        RegisterController registerController = new RegisterController();

        // when
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("solong");
        when(request.getParameter("email")).thenReturn("solong@woowahan.com");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final ModelAndView modelAndView = registerController.save(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("회원 가입을 한다. - 실패, 중복된 account")
    @Test
    void saveFailed() {
        // given
        RegisterController registerController = new RegisterController();

        // when
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("email")).thenReturn("solong@woowahan.com");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final ModelAndView modelAndView = registerController.save(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("/400.jsp");
    }

    @DisplayName("회원가입 페이지를 보여준다.")
    @Test
    void show() {
        // given
        RegisterController registerController = new RegisterController();

        // when
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final ModelAndView modelAndView = registerController.show(request, response);

        // then
        assertThat(modelAndView.view().viewName()).isEqualTo("/register.jsp");
    }
}
