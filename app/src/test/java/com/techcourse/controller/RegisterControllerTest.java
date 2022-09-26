package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    private final RegisterController controller = new RegisterController();

    @DisplayName("/register 요청 시 ")
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
            final String viewName = controller.execute(request, response);

            // then
            assertThat(viewName).isEqualTo("redirect:/register.jsp");
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
            final String viewName = controller.execute(request, response);

            // then
            assertThat(viewName).isEqualTo("redirect:/register.jsp");
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
            final String viewName = controller.execute(request, response);

            // then
            assertThat(viewName).isEqualTo("redirect:/register.jsp");
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
            final String viewName = controller.execute(request, response);
            final boolean isRegistered = InMemoryUserRepository.findByAccount("gugu2").isPresent();

            // then
            assertAll(
                    () -> assertThat(viewName).isEqualTo("redirect:/index.jsp"),
                    () -> assertThat(isRegistered).isTrue()
            );
        }
    }
}
