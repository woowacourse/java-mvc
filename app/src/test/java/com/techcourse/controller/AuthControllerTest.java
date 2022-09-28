package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.techcourse.support.Fixture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AuthControllerTest {

    private final AuthController controller = new AuthController();

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
            final String viewName = controller.loginView(request, response);

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
            final String viewName = controller.loginView(request, response);

            // then
            assertAll(
                    () -> verify(request, never()).getParameter("account"),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp")
            );
        }
    }

    @DisplayName("/login 요청 시")
    @Nested
    class Login {

        @DisplayName("비밀번호가 일치하면 viewName으로 redirect:/index.jsp 가 응답된다")
        @Test
        void redirect_to_index_when_password_is_correct() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final HttpSession session = mock(HttpSession.class);
            given(request.getParameter("account")).willReturn("gugu");
            given(request.getParameter("password")).willReturn("password");
            given(request.getSession()).willReturn(session);

            // when
            final String viewName = controller.login(request, response);

            // then
            assertThat(viewName).isEqualTo("redirect:/index.jsp");
        }

        @DisplayName("비밀번호가 일치하지 않으면 viewName으로 redirect:/401.jsp 가 응답된다")
        @Test
        void redirect_to_401_when_password_is_not_correct() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final HttpSession session = mock(HttpSession.class);
            given(request.getParameter("account")).willReturn("gugu");
            given(request.getParameter("password")).willReturn("not_correct_password");
            given(request.getSession()).willReturn(session);

            // when
            final String viewName = controller.login(request, response);

            // then
            assertThat(viewName).isEqualTo("redirect:/401.jsp");
        }

        @DisplayName("이미 로그인되어있다면 getAttribute메서드 호출 없이 viewName으로 redirect:/index.jsp 가 응답된다")
        @Test
        void redirect_to_index_when_already_logged_in() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final HttpSession session = mock(HttpSession.class);
            given(request.getSession()).willReturn(session);
            given(session.getAttribute("user")).willReturn(Fixture.GUGU_FIXTURE);

            // when
            final String viewName = controller.login(request, response);

            // then
            assertAll(
                    () -> verify(request, never()).getParameter("account"),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp")
            );
        }
    }

    @DisplayName("/logout 요청 시 session.removeAttribute가 호출되고, viewName으로 redirect:/ 가 응답된다")
    @Test
    void logout_should_return_root_page_with_calling_removeAttribute() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        given(request.getSession()).willReturn(session);

        // when
        final String viewName = controller.logout(request, response);

        // then
        assertAll(
                () -> verify(session, times(1)).removeAttribute("user"),
                () -> assertThat(viewName).isEqualTo("redirect:/")
        );
    }
}
