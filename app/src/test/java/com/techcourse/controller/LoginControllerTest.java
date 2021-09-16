package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.techcourse.session.UserSession.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("LoginController 테스트")
class LoginControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private LoginController loginController;
    private User user;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        loginController = new LoginController();
        user = new User(1L, "gugu", "password", "hkkang@woowahan.com");
    }

    @DisplayName("Get 요청")
    @Nested
    class get {

        @DisplayName("LoginController GET /login 요청 테스트 - 로그인 되어있는 경우")
        @Test
        void getLoginWhenLoggedIn() {
            // given
            loggedIn();

            // when
            final ModelAndView modelAndView = loginController.loginView(request, response);
            final View view = modelAndView.getView();

            // then
            assertThat(view).isInstanceOf(JspView.class);
        }

        @DisplayName("LoginController GET /login 요청 테스트 - 로그인이 되어있지 않은 경우")
        @Test
        void getLoginWhenNotLoggedIn() {
            // given
            notLoggedIn();

            // when
            final ModelAndView modelAndView = loginController.loginView(request, response);
            final View view = modelAndView.getView();

            // then
            assertThat(view).isInstanceOf(JspView.class);
        }
    }

    @DisplayName("Post 요청")
    @Nested
    class post {

        @DisplayName("LoginController POST /login 요청 테스트 - 로그인 되어있는 경우")
        @Test
        void postLoginWhenLoggedIn() {
            // given
            loggedIn();

            // when
            final ModelAndView modelAndView = loginController.login(request, response);
            final View view = modelAndView.getView();

            // then
            assertThat(view).isInstanceOf(JspView.class);
        }

        @DisplayName("LoginController POST /login 요청 테스트 - 로그인 되어있지 않은 경우")
        @Test
        void postLoginWhenNotLoggedIn() {
            // given
            notLoggedIn();
            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("password");

            // when
            final ModelAndView modelAndView = loginController.login(request, response);
            final View view = modelAndView.getView();

            // then
            assertThat(view).isInstanceOf(JspView.class);
        }
    }

    private void loggedIn() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_KEY)).thenReturn(user);
    }

    private void notLoggedIn() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_KEY)).thenReturn(null);
    }
}