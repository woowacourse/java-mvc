package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterIntegrationTest {

    private final DispatcherServlet servlet = new DispatcherServlet();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final String account = "register-test-" + UUID.randomUUID();

    @BeforeEach
    void setUp() {
        servlet.init();
    }

    @Test
    @DisplayName("GET 회원가입 요청은 회원을 저장하지 않고 가입 화면을 보여준다")
    void getShowsFormWithoutSavingUser() throws Exception {
        registrationRequest("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
    }

    @Test
    @DisplayName("POST 회원가입 요청은 회원을 저장하고 인덱스 화면으로 리다이렉트한다")
    void postSavesUserAndRedirects() throws Exception {
        registrationRequest("POST");

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account)).hasValueSatisfying(user -> {
            assertThat(user.getAccount()).isEqualTo(account);
            assertThat(user.checkPassword("password")).isTrue();
        });
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("PUT 회원가입 요청은 회원을 저장하지 않고 404를 반환한다")
    void putReturnsNotFoundWithoutSavingUser() throws Exception {
        registrationRequest("PUT");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
    }

    @Test
    @DisplayName("기존 회원가입 화면 주소는 새 회원가입 주소로 리다이렉트한다")
    void oldFormUrlRedirectsToNewUrl() throws Exception {
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendRedirect("/register");
    }

    @Test
    @DisplayName("애노테이션 회원가입과 함께 레거시 홈 화면도 정상 동작한다")
    void legacyHomeStillWorksAlongsideRegistration() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    private void registrationRequest(final String method) {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(method);
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("register@example.com");
    }
}
