package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoginControllerTest {

    @Test
    void 로그인_성공_테스트() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        LoginController loginController = new LoginController();
        String execute = loginController.execute(request, response);

        Assertions.assertThat(execute).isEqualTo("redirect:/index.jsp");
    }

    @Test
    void 로그인_실패_테스트() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("account")).thenReturn("power");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        LoginController loginController = new LoginController();
        String execute = loginController.execute(request, response);

        Assertions.assertThat(execute).isEqualTo("redirect:/401.jsp");
    }

    @Test
    void 세션_존재_할때_로그인_테스트() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(session.getAttribute("user")).thenReturn(new User("power","power","power@naver.com"));
        when(request.getSession()).thenReturn(session);

        LoginController loginController = new LoginController();
        String execute = loginController.execute(request, response);

        Assertions.assertThat(execute).isEqualTo("redirect:/index.jsp");
    }
}
