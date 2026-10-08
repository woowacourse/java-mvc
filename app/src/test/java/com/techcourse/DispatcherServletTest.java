package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.web.http.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
    }

    @Test
    @DisplayName("GET / 요청은 메인 화면으로 포워드한다")
    void rootForwardsToIndex() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /register/view 요청은 가입 화면으로 포워드한다")
    void registrationViewForwardsToRegisterPage() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /api/user 요청은 회원 정보를 JSON으로 응답한다")
    void userApiRespondsWithJson() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString())
                .contains("\"account\":\"gugu\"")
                .contains("\"email\":\"hkkang@woowahan.com\"")
                .doesNotContain("password");
    }

    @Test
    @DisplayName("GET /api/user 요청에 없는 계정을 보내면 404로 응답한다")
    void unknownUserApiReturns404() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("nobody");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "존재하지 않는 계정입니다: nobody");
    }

    @Test
    @DisplayName("레거시 매핑이 사라진 뒤에는 GET만 허용하는 URL에 다른 메서드로 요청하면 405로 응답한다")
    void unsupportedMethodOnViewPathReturns405() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("PATCH");

        servlet.service(request, response);

        verify(response).setHeader("Allow", "GET");
        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @DisplayName("열거형에 없는 메서드도 URL이 없으면 404로 응답한다")
    void extensionHttpMethodWithUnknownPathReturns404() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("BREW");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("등록되지 않은 URL은 404로 응답한다")
    void unknownPathReturns404() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("URL은 있지만 허용하지 않는 HTTP 메서드는 Allow 헤더와 함께 405로 응답한다")
    void unsupportedMethodReturns405() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("PUT");

        servlet.service(request, response);

        verify(response).setHeader("Allow", "GET, POST");
        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @DisplayName("열거형에 없는 메서드도 URL에 메서드 제한이 있으면 405로 응답한다")
    void extensionHttpMethodWithRestrictedPathReturns405() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("BREW");

        servlet.service(request, response);

        verify(response).setHeader("Allow", "GET, POST");
        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @DisplayName("GET 요청은 애노테이션 컨트롤러의 GET 메서드를 실행한다")
    void annotatedGetIsDispatched() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("registrationSource", "annotation-get");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("POST 요청은 애노테이션 컨트롤러의 POST 메서드를 실행한다")
    void annotatedPostIsDispatched() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendRedirect("/annotation-complete");
    }

    @Test
    @DisplayName("요청 처리 중 발생한 예외를 ServletException의 원인으로 보존한다")
    void preservesOriginalException() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        final var original = new IOException("forward failed");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
        doThrow(original).when(dispatcher).forward(request, response);

        ServletException thrown = assertThrows(ServletException.class, () -> servlet.service(request, response));

        assertThat(thrown.getCause()).isSameAs(original);
    }
}

