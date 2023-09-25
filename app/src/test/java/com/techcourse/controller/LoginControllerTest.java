package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static webmvc.org.springframework.web.servlet.view.JspView.REDIRECT_PREFIX;

class LoginControllerTest {

    private LoginController loginController = new LoginController();

    @Test
    void 올바른_정보로_로그인할_때_인덱스를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        HttpSession httpSession = mock(HttpSession.class);
        doNothing().when(httpSession).setAttribute(any(), any());
        when(request.getSession()).thenReturn(httpSession);


        //when
        ModelAndView modelAndView = loginController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView(REDIRECT_PREFIX + "/index.jsp"));
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 잘못된_정보로_로그인할_때_401을_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrongPassword");

        HttpSession httpSession = mock(HttpSession.class);
        doNothing().when(httpSession).setAttribute(any(), any());
        when(request.getSession()).thenReturn(httpSession);

        //when
        ModelAndView modelAndView = loginController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView(REDIRECT_PREFIX + "/401.jsp"));
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 이미_로그인_돼있는_경우_인덱스를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1, "gugu", "password", "hkkang@woowahan.com"));

        //when
        ModelAndView modelAndView = loginController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView(REDIRECT_PREFIX + "/index.jsp"));
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
