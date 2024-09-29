package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    @DisplayName("세션이 있는 경우 /login 경로로 GET 요청시 redirect:/ 를 반환한다.")
    @Test
    void showWithSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        LoginController loginController = new LoginController();

        HttpSession session = mock(HttpSession.class);
        given(request.getSession()).willReturn(session);
        User user = new User(1L, "", "", "");
        given(session.getAttribute(UserSession.SESSION_KEY)).willReturn(user);

        ModelAndView modelAndView = loginController.show(request, response);

        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("redirect:/")));
    }

    @DisplayName("세션없이 /login 경로로 GET 요청시 login.jsp 를 반환한다.")
    @Test
    void showWithoutSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        LoginController loginController = new LoginController();

        HttpSession session = mock(HttpSession.class);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute(UserSession.SESSION_KEY)).willReturn(null);

        ModelAndView modelAndView = loginController.show(request, response);

        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("login.jsp")));
    }
}
