package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.RequestDispatcher;
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

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("로그인 화면과 로그아웃 요청의 MVC 연결")
class SessionNavigationFlowTest {

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
        when(request.getSession()).thenReturn(session);
        servlet = new DispatcherServletInitializer().createDispatcherServlet();
        servlet.init(mock(ServletConfig.class));
    }

    @Nested
    @DisplayName("GET /login/view")
    class LoginPage {

        @BeforeEach
        void setUpRequest() {
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/login/view");
        }

        @Test
        @DisplayName("로그인하지 않았으면 로그인 JSP로 전달한다")
        void forwardsToLoginPageWhenLoggedOut() throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

            // when
            servlet.service(request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @Test
        @DisplayName("이미 로그인했으면 인덱스로 리다이렉트한다")
        void redirectsToIndexWhenLoggedIn() throws Exception {
            // given
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(USER);

            // when
            servlet.service(request, response);

            // then
            verify(response).sendRedirect("/index.jsp");
        }
    }

    @Nested
    @DisplayName("GET /logout")
    class Logout {

        @BeforeEach
        void setUpRequest() {
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/logout");
        }

        @Test
        @DisplayName("세션의 로그인 사용자 속성을 제거한다")
        void removesSessionUser() throws Exception {
            // when
            servlet.service(request, response);

            // then
            verify(session).removeAttribute(UserSession.SESSION_KEY);
        }

        @Test
        @DisplayName("로그아웃 후 루트 경로로 리다이렉트한다")
        void redirectsToRoot() throws Exception {
            // when
            servlet.service(request, response);

            // then
            verify(response).sendRedirect("/");
        }

        @Test
        @DisplayName("세션 전체를 무효화하지 않는다")
        void doesNotInvalidateSession() throws Exception {
            // when
            servlet.service(request, response);

            // then
            verify(session, never()).invalidate();
        }
    }

    @Nested
    @DisplayName("지원하지 않는 HTTP 메서드")
    class UnsupportedMethod {

        @DisplayName("GET 이외의 요청은 수동 매핑으로 넘어가지 않고 404를 응답한다")
        @ParameterizedTest(name = "[{index}] {0} {1} → 매핑 없음")
        @MethodSource("unsupportedRequests")
        void doesNotFallBackToManualMapping(final String method, final String path) throws Exception {
            // given
            when(request.getMethod()).thenReturn(method);
            when(request.getRequestURI()).thenReturn(path);
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(USER);

            // when
            servlet.service(request, response);

            // then
            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        @DisplayName("매핑되지 않은 요청은 세션에 접근하지 않는다")
        @ParameterizedTest(name = "[{index}] {0} {1} → 세션 접근 없음")
        @MethodSource("unsupportedRequests")
        void doesNotAccessSessionForUnsupportedRequest(final String method, final String path) throws Exception {
            // given
            when(request.getMethod()).thenReturn(method);
            when(request.getRequestURI()).thenReturn(path);
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(USER);

            // when
            servlet.service(request, response);

            // then
            verify(request, never()).getSession();
        }

        static Stream<Arguments> unsupportedRequests() {
            return Stream.of("/login/view", "/logout")
                    .flatMap(path -> Stream.of("POST", "PUT", "DELETE")
                            .map(method -> Arguments.of(method, path)));
        }
    }
}
