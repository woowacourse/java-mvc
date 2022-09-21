package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.support.CustomReflectionUtils;
import com.techcourse.support.Fixture;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnnotationControllerTest {

    private final AnnotationController controller = new AnnotationController();

    @DisplayName("/@mvc/index 요청시 viewName으로 /index.jsp 가 응답된다")
    @Test
    void index() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        final ModelAndView modelAndView = controller.index(request, response);

        final View view = modelAndView.getView();
        final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

        // then
        assertAll(
                () -> assertThat(view).isInstanceOf(JspView.class),
                () -> assertThat(viewName).isEqualTo("/index.jsp")
        );
    }

    @DisplayName("/@mvc/login 요청 시")
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
            final ModelAndView modelAndView = controller.login(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp")
            );
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
            final ModelAndView modelAndView = controller.login(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/401.jsp")
            );
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
            final ModelAndView modelAndView = controller.login(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> verify(request, never()).getParameter("account"),
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp")
            );
        }
    }

    @DisplayName("@/mvc/login/view 요청시")
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
            final ModelAndView modelAndView = controller.loginView(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("/login.jsp")
            );
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
            final ModelAndView modelAndView = controller.loginView(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> verify(request, never()).getParameter("account"),
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp")
            );
        }
    }

    @DisplayName("/@mvc/logout 요청 시 session.removeAttribute가 호출되고, viewName으로 redirect:/ 가 응답된다")
    @Test
    void logout_should_return_root_page_with_calling_removeAttribute() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        given(request.getSession()).willReturn(session);

        // when
        final ModelAndView modelAndView = controller.logout(request, response);

        final View view = modelAndView.getView();
        final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

        // then
        assertAll(
                () -> verify(session, times(1)).removeAttribute("user"),
                () -> assertThat(view).isInstanceOf(JspView.class),
                () -> assertThat(viewName).isEqualTo("redirect:/")
        );
    }

    @DisplayName("/@mvc/register/view 요청 시 viewName으로 /register.jsp 가 응답된다")
    @Test
    void registerView_should_return_register_jsp() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        final ModelAndView modelAndView = controller.registerView(request, response);

        final View view = modelAndView.getView();
        final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

        // then
        assertAll(
                () -> assertThat(view).isInstanceOf(JspView.class),
                () -> assertThat(viewName).isEqualTo("/register.jsp")
        );
    }

    @DisplayName("/@mvc/register 요청 시 ")
    @Nested
    class Register {

        @DisplayName("account 누락 시 viewName으로 redirect:/register.jsp 가 응답된다")
        @Test
        void register_should_return_register_jsp_if_account_is_absent() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            given(request.getParameter("password")).willReturn("password");
            given(request.getParameter("email")).willReturn("hkkang@woowahan.com");

            // when
            final ModelAndView modelAndView = controller.register(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/register.jsp")
            );
        }

        @DisplayName("password 누락 시 viewName으로 redirect:/register.jsp 가 응답된다")
        @Test
        void register_should_return_register_jsp_if_password_is_absent() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            given(request.getParameter("account")).willReturn("gugu");
            given(request.getParameter("email")).willReturn("hkkang@woowahan.com");

            // when
            final ModelAndView modelAndView = controller.register(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/register.jsp")
            );
        }

        @DisplayName("email 누락 시 viewName으로 redirect:/register.jsp 가 응답된다")
        @Test
        void register_should_return_register_jsp_if_password_is_email() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            given(request.getParameter("account")).willReturn("gugu");
            given(request.getParameter("password")).willReturn("password");

            // when
            final ModelAndView modelAndView = controller.register(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/register.jsp")
            );
        }

        @DisplayName("account, password, email 정상 전달 시 유저가 저장되고, viewName으로 redirect:/index.jsp 가 응답된다")
        @Test
        void register_should_return_index_jsp_if_essentials_are_present_with_user_registered() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            given(request.getParameter("account")).willReturn("gugu2");
            given(request.getParameter("password")).willReturn("password");
            given(request.getParameter("email")).willReturn("hkkang2@woowahan.com");

            // when
            final ModelAndView modelAndView = controller.register(request, response);

            final View view = modelAndView.getView();
            final String viewName = CustomReflectionUtils.readFieldValue(view, "viewName");
            final boolean isRegistered = InMemoryUserRepository.findByAccount("gugu2").isPresent();

            // then
            assertAll(
                    () -> assertThat(view).isInstanceOf(JspView.class),
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp"),
                    () -> assertThat(isRegistered).isTrue()
            );
        }
    }
}
