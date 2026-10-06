package com.techcourse;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("회원가입 요청의 MVC 연결")
class RegistrationFlowTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Nested
    @DisplayName("어노테이션 방식의 회원가입")
    class AnnotatedRegistration {

        @Test
        @DisplayName("GET /register 요청은 회원가입 화면으로 전달한다")
        void forwardsGetRequestToRegistrationView() throws Exception {
            // given
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/register");
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

            // when
            servlet.service(request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @Test
        @DisplayName("GET /register 요청은 사용자를 저장하지 않는다")
        void doesNotSaveUserOnGetRequest() throws Exception {
            // given
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getRequestDispatcher("/register.jsp")).thenReturn(mock(RequestDispatcher.class));
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                servlet.service(request, response);

                // then
                repository.verify(() -> InMemoryUserRepository.save(any()), never());
            }
        }

        @Test
        @DisplayName("POST /register 요청은 회원가입 컨트롤러를 통해 사용자를 저장한다")
        void savesUserThroughRegistrationController() throws Exception {
            // given
            when(request.getMethod()).thenReturn("POST");
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getParameter("account")).thenReturn("new-user");
            when(request.getParameter("password")).thenReturn("password");
            when(request.getParameter("email")).thenReturn("user@example.com");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                servlet.service(request, response);

                // then
                repository.verify(() -> InMemoryUserRepository.save(any(User.class)));
            }
        }

        @Test
        @DisplayName("POST /register 요청을 처리한 뒤 메인 화면으로 리다이렉트한다")
        void redirectsPostRequestAfterRegistration() throws Exception {
            // given
            when(request.getMethod()).thenReturn("POST");
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getParameter("account")).thenReturn("new-user");
            when(request.getParameter("password")).thenReturn("password");
            when(request.getParameter("email")).thenReturn("user@example.com");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                servlet.service(request, response);

                // then
                verify(response).sendRedirect("/index.jsp");
            }
        }
    }

    @Nested
    @DisplayName("지원하지 않는 HTTP 메서드")
    class UnsupportedMethod {

        @BeforeEach
        void setUpRequest() {
            when(request.getMethod()).thenReturn("PUT");
            when(request.getRequestURI()).thenReturn("/register");
        }

        @Test
        @DisplayName("PUT /register 요청은 매핑이 없으므로 404를 응답한다")
        void sendsNotFoundForUnsupportedMethod() throws Exception {
            // when
            servlet.service(request, response);

            // then
            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        @Test
        @DisplayName("PUT /register 요청이 기존 수동 Controller로 넘어가 저장되지 않는다")
        void doesNotFallBackToLegacyRegistration() throws Exception {
            // given
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                servlet.service(request, response);

                // then
                repository.verifyNoInteractions();
            }
        }
    }

    @Nested
    @DisplayName("기존 경로와의 호환")
    class LegacyCompatibility {

        @Test
        @DisplayName("기존 GET /register/view 요청도 회원가입 화면을 제공한다")
        void preservesLegacyRegistrationView() throws Exception {
            // given
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/register/view");
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

            // when
            servlet.service(request, response);

            // then
            verify(dispatcher).forward(request, response);
        }
    }
}
