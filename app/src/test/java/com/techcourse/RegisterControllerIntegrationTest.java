package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterControllerIntegrationTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void GET_회원가입_요청은_가입_화면을_보여준다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 기존_회원가입_화면_주소도_유지한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 어노테이션으로_가입한_회원이_기존_컨트롤러로_로그인한다() throws Exception {
        final var account = "register-test-" + UUID.randomUUID();
        final var registerRequest = mock(HttpServletRequest.class);
        final var registerResponse = mock(HttpServletResponse.class);
        when(registerRequest.getRequestURI()).thenReturn("/register");
        when(registerRequest.getMethod()).thenReturn("POST");
        when(registerRequest.getParameter("account")).thenReturn(account);
        when(registerRequest.getParameter("password")).thenReturn("password");
        when(registerRequest.getParameter("email")).thenReturn("register@example.com");

        dispatcherServlet.service(registerRequest, registerResponse);

        final var savedUser = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertThat(savedUser).usingRecursiveComparison()
                .isEqualTo(new User(2, account, "password", "register@example.com"));
        verify(registerResponse).sendRedirect("/index.jsp");

        final var loginRequest = mock(HttpServletRequest.class);
        final var loginResponse = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(loginRequest.getRequestURI()).thenReturn("/login");
        when(loginRequest.getMethod()).thenReturn("POST");
        when(loginRequest.getParameter("account")).thenReturn(account);
        when(loginRequest.getParameter("password")).thenReturn("password");
        when(loginRequest.getSession()).thenReturn(session);

        dispatcherServlet.service(loginRequest, loginResponse);

        verify(session).setAttribute(UserSession.SESSION_KEY, savedUser);
        verify(loginResponse).sendRedirect("/index.jsp");
    }

    @Test
    void 회원가입에_등록되지_않은_HTTP_메서드는_회원을_저장하지_않는다() throws Exception {
        final var account = "unsupported-register-test-" + UUID.randomUUID();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("register@example.com");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
    }
}
