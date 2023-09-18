package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginViewControllerTest {
    private final LoginViewController loginViewController = new LoginViewController();

    @Test
    @DisplayName("로그인 안되있을 경우 로그인 페이지 반환")
    void getLoginPageWithOutlogin() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        //when
        final ModelAndView modelAndView = new ModelAndView(new JspView(loginViewController.execute(request, response)));

        //then
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(new ModelAndView(new JspView("/login.jsp")));
    }

    @Test
    @DisplayName("로그인 되있을 경우 인덱스 페이지 반환")
    void getLoginPageWithLogin() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1, "gugu", "password", "gugu@email.com"));

        //when
        final ModelAndView modelAndView = new ModelAndView(new JspView(loginViewController.execute(request, response)));

        //then
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(new ModelAndView(new JspView("redirect:" + "/index.jsp")));
    }
}
