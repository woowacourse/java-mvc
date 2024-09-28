package com.techcourse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class DispatcherServletTest {

    private DispatcherServlet sut;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        sut = new DispatcherServlet();
        sut.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Nested
    @DisplayName("Controller 인터페이스 기반 컨트롤러에 대한 요청을 처리한다.")
    class ControllerInterface {
        @Test
        @DisplayName("login에 대한 요청을 처리한다.")
        void handleLogin() throws IOException, ServletException {
            // given
            final var session = mock(HttpSession.class);
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("password");
            when(request.getRequestURI()).thenReturn("/login");
            when(request.getMethod()).thenReturn("POST");
            when(request.getSession()).thenReturn(session);
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            // when
            sut.service(request, response);

            // then
            verify(response).sendRedirect("/index.jsp");
        }
    }

    @Nested
    @DisplayName("Controller 어노테이션 기반 컨트롤러에 대한 요청을 처리한다.")
    class ControllerAnnotation {

        @Test
        @DisplayName("/register에 대한 GET 요청을 처리한다.")
        void handleRegisterGET() throws ServletException, IOException {
            // given
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getRequestURI()).thenReturn("/register");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            // when
            sut.service(request, response);

            // then
            verify(requestDispatcher).forward(request, response);
        }

        @Test
        @DisplayName("/register에 대한 POST 요청을 처리한다.")
        void handleRegisterPOST() throws ServletException, IOException {
            // given
            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("email")).thenReturn("gugu@email.com");
            when(request.getParameter("password")).thenReturn("password");
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getMethod()).thenReturn("POST");

            // when
            sut.service(request, response);

            // then
            verify(response).sendRedirect("/index.jsp"); // 리다이렉트 확인
        }
    }
}
