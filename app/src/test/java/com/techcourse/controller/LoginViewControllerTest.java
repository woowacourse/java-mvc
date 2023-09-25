package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginViewControllerTest {

    private LoginViewController loginViewController = new LoginViewController();

    @Test
    void 로그인되지_않은_경우_로그인_페이지를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        //when
        final ModelAndView actual = loginViewController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView("/login.jsp"));
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 로그인이_되어있는_경우_인덱스를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1, "gugu", "password", "gugu@email.com"));

        //when
        final ModelAndView actual = loginViewController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView("/index.jsp"));
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

}
