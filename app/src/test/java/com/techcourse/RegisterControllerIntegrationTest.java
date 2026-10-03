package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterControllerIntegrationTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Test
    @DisplayName("GET /register는 회원을 저장하지 않고 가입 화면을 보여준다")
    void showRegistrationForm() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        final var account = "register-get-" + UUID.randomUUID();
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("get@example.com");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("POST /register는 회원을 저장하고 메인 화면으로 이동한다")
    void saveRegistration() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var account = "register-post-" + UUID.randomUUID();
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("post@example.com");

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account)).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }
}
