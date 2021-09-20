package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("로그인 컨트롤러 테스트")
class LoginControllerTest {

    private HttpServletRequest request;
    private HttpSession session;
    private LoginController loginController = new LoginController();

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
    }

    @DisplayName("Post 요청 시")
    @Nested
    class LoginPost {

        @BeforeEach
        void setup() {
            when(request.getSession()).thenReturn(session);
        }

        @DisplayName("비밀번호가 틀리면 /401.jsp로 리다이렉트")
        @Test
        void passwordWrongTest() {
            //given
            when(session.getAttribute("user")).thenReturn(null);

            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("wrong");

            String expectedResult = "redirect:/401.jsp";
            //when
            String destination = loginController.login(request);
            //then
            assertThat(destination).isEqualTo(expectedResult);
        }

        @DisplayName("로그인에 성공하면 /index.jsp로 리다이렉트")
        @Test
        void loginSuccessTest() {
            //given
            when(session.getAttribute("user")).thenReturn(null);

            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("password");

            String expectedResult = "redirect:/index.jsp";
            //when
            String destination = loginController.login(request);
            //then
            assertThat(destination).isEqualTo(expectedResult);
        }

        @DisplayName("세션이 존재하면 /index.jsp로 리다이렉트")
        @Test
        void sessionTest() {
            //given
            when(session.getAttribute("user"))
                .thenReturn(
                    new User("login", "login", "login")
                );

            String expectedResult = "redirect:/index.jsp";
            //when
            String destination = loginController.login(request);
            //then
            assertThat(destination).isEqualTo(expectedResult);
        }
    }
}