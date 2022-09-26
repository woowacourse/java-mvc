package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.techcourse.support.Fixture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LoginViewControllerTest {

    private final LoginViewController controller = new LoginViewController();

    @DisplayName("/login/view 요청시")
    @Nested
    class LoginView {

        @DisplayName("로그인 되어있지 않다면 viewName으로 /login.jsp를 응답한다")
        @Test
        void loginView_should_return_login_jsp_if_not_logged_in() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final HttpSession session = mock(HttpSession.class);
            given(request.getParameter("account")).willReturn("gugu");
            given(request.getSession()).willReturn(session);

            // when
            final String viewName = controller.execute(request, response);

            // then
            assertThat(viewName).isEqualTo("/login.jsp");
        }

        @DisplayName("로그인 되어있다면 viewName으로 redirect:/index.jsp를 응답한다")
        @Test
        void loginView_should_return_index_jsp_if_logged_in() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final HttpSession session = mock(HttpSession.class);
            given(request.getSession()).willReturn(session);
            given(session.getAttribute("user")).willReturn(Fixture.GUGU_FIXTURE);

            // when
            final String viewName = controller.execute(request, response);

            // then
            assertAll(
                    () -> verify(request, never()).getParameter("account"),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp")
            );
        }
    }
}
