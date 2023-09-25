package com.techcourse.controller;

import com.techcourse.LoginController;
import com.techcourse.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    @Test
    void 세션에_유저_없을_때_로그인_성공_테스트() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse res = mock(HttpServletResponse.class);
        final HttpSession httpSession = mock(HttpSession.class);
        when(req.getParameter("account")).thenReturn("gugu");
        when(req.getParameter("password")).thenReturn("password");
        when(req.getSession()).thenReturn(httpSession);

        LoginController loginController = new LoginController();
        ModelAndView execute = loginController.execute(req, res);

        Assertions.assertThat(execute.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/index.jsp"));
    }

    @Test
    void 세션에_유저정보_있을_때_리다이렉트_테스트() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse res = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(new User(1, "gugu", "password", "gugu@naver.com"));
        when(req.getSession()).thenReturn(session);
//        when(UserSession.isLoggedIn(session)).thenReturn(true);

        LoginController loginController = new LoginController();
        ModelAndView execute = loginController.execute(req, res);

        Assertions.assertThat(execute.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/index.jsp"));
    }
}
