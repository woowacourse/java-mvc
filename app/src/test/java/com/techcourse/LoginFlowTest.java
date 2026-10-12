package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("로그인 요청의 어노테이션 MVC 연결")
class LoginFlowTest {

    private static final User USER = new User(1, "gugu", "password", "gugu@example.com");

    private Servlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() throws ServletException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        servlet = new DispatcherServletInitializer().createDispatcherServlet();
        servlet.init(mock(ServletConfig.class));
    }

    @Nested
    @DisplayName("POST /login")
    class SubmitLogin {

        @BeforeEach
        void setUpPostRequest() {
            when(request.getMethod()).thenReturn("POST");
            when(request.getParameter("account")).thenReturn("gugu");
        }

        @Test
        @DisplayName("인증에 성공하면 세션에 조회한 사용자를 저장한다")
        void storesAuthenticatedUserInSession() throws Exception {
            // given
            when(request.getParameter("password")).thenReturn("password");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.of(USER));

                // when
                servlet.service(request, response);

                // then
                verify(session).setAttribute(UserSession.SESSION_KEY, USER);
            }
        }

        @DisplayName("인증에 실패하면 401 페이지로 리다이렉트한다")
        @ParameterizedTest(name = "[{index}] POST /login 인증 실패: {0}")
        @MethodSource("failedCredentials")
        void redirectsFailedLogin(final String caseName, final Optional<User> user,
                                  final String password) throws Exception {
            // given
            when(request.getParameter("password")).thenReturn(password);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(user);

                // when
                servlet.service(request, response);

                // then
                verify(response).sendRedirect("/401.jsp");
            }
        }

        static Stream<Arguments> failedCredentials() {
            return Stream.of(
                    Arguments.of("존재하지 않는 계정", Optional.empty(), "password"),
                    Arguments.of("잘못된 비밀번호", Optional.of(USER), "wrong-password")
            );
        }

        @Test
        @DisplayName("이미 로그인했으면 다시 인증하지 않고 인덱스로 리다이렉트한다")
        void redirectsAlreadyLoggedInUser() throws Exception {
            // given
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(USER);

            // when
            servlet.service(request, response);

            // then
            verify(response).sendRedirect("/index.jsp");
        }
    }

    @Nested
    @DisplayName("지원하지 않는 HTTP 메서드")
    class UnsupportedMethod {

        @DisplayName("POST 이외의 요청은 수동 로그인 처리로 넘어가지 않고 404를 응답한다")
        @ParameterizedTest(name = "[{index}] {0} /login → 매핑 없음")
        @ValueSource(strings = {"GET", "PUT", "DELETE"})
        void doesNotFallBackToManualLogin(final String method) throws Exception {
            // given
            when(request.getMethod()).thenReturn(method);
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(USER);

            // when
            servlet.service(request, response);

            // then
            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
