package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("로그아웃 컨트롤러 테스트")
class LogoutControllerTest {

    private HttpServletRequest request;
    private HttpSession session;
    private LogoutController logoutController = new LogoutController();

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
    }

    @DisplayName("Get 요청 시")
    @Nested
    class LogoutGet {

        @BeforeEach
        void setup() {
            when(request.getSession()).thenReturn(session);
        }

        @DisplayName("비밀번호가 틀리면 루트 url로 리다이렉트하고 세션을 초기화하는지 확인")
        @Test
        void passwordWrongTest() {
            //given
            String expectedResult = "redirect:/";
            //when
            String destination = logoutController.logOut(request);
            //then
            verify(session, atLeastOnce()).removeAttribute("user");
            assertThat(destination).isEqualTo(expectedResult);
        }
    }
}