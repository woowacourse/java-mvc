package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("로그인 뷰 컨트롤러 테스트")
public class LoginViewControllerTest {

    private HttpSession session;
    private LoginViewController loginViewController = new LoginViewController();

    @BeforeEach
    void setUp() {
        session = mock(HttpSession.class);
    }

    @DisplayName("Get 요청 시")
    @Nested
    class LoginViewGet {

        @DisplayName("login.jsp를 출력한다.")
        @Test
        void loginViewTest() {
            //given
            when(session.getAttribute("user")).thenReturn(null);


            String expectedResult = "/login.jsp";
            //when
            String destination = loginViewController.getLoginView(session);
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
            String destination = loginViewController.getLoginView(session);
            //then
            assertThat(destination).isEqualTo(expectedResult);
        }
    }
}
