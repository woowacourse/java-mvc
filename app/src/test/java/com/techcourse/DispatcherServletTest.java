package com.techcourse;

import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class DispatcherServletTest {

    private static MockedStatic<UserSession> userSession;
    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeAll
    static void sessionInit() {
        userSession = mockStatic(UserSession.class);
    }

    @AfterAll
    static void sessionClose() {
        userSession.close();
    }

    @BeforeEach
    void servletInit() {
        dispatcherServlet.init();
    }

    @Nested
    class ServiceTest {
        @Test
        @DisplayName("요청 테스트 - /")
        void index() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

            //when
            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

            //then
            assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        }

        @Test
        @DisplayName("요청 테스트 - /login")
        void login() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

            //when
            when(request.getRequestURI()).thenReturn("/login");
            when(request.getMethod()).thenReturn("POST");
            when(request.getRequestDispatcher("/login")).thenReturn(requestDispatcher);
            when(UserSession.isLoggedIn(any())).thenReturn(true);

            //then
            assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        }

        @Test
        @DisplayName("요청 테스트 - /login/view")
        void login_view() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

            //when
            when(request.getRequestURI()).thenReturn("/login/view");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

            //then
            assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        }

        @Test
        @DisplayName("요청 테스트 - /logout")
        void logout() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
            final HttpSession httpSession = mock(HttpSession.class);

            //when
            when(request.getRequestURI()).thenReturn("/logout");
            when(request.getMethod()).thenReturn("GET");
            when(request.getSession()).thenReturn(httpSession);
            when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

            //then
            assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        }

        @Test
        @DisplayName("요청 테스트 - /register/view")
        void register_view() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

            //when
            when(request.getRequestURI()).thenReturn("/register/view");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

            //then
            assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        }

        @Test
        @DisplayName("요청 테스트 - /register")
        void register() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

            //when
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getMethod()).thenReturn("POST");
            when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
            when(request.getParameter("account")).thenReturn("id");

            //then
            assertDoesNotThrow(() -> dispatcherServlet.service(request, response));
        }

        @Test
        @DisplayName("핸들러가 존재하지 않는 요청 URL이면 예외가 발생한다")
        void service_fail() {
            //given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            //when
            when(request.getRequestURI()).thenReturn("/nothing");
            when(request.getMethod()).thenReturn("GET");

            //then
            assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }
}
