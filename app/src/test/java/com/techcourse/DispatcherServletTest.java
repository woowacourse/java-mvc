package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void 어노테이션_컨트롤러로_회원가입_화면을_표시한다() throws Exception {
        HttpServletRequest request = request("GET", "/register");
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher viewDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(viewDispatcher);

        dispatcherServlet.service(request, response);

        verify(viewDispatcher).forward(request, response);
    }

    @Test
    void 어노테이션_회원가입과_기존_로그인_로그아웃이_공존한다() throws Exception {
        String account = "mvc-integration-user";
        String password = "test-password";
        HttpServletRequest registerRequest = request("POST", "/register");
        HttpServletResponse registerResponse = mock(HttpServletResponse.class);
        when(registerRequest.getParameter("account")).thenReturn(account);
        when(registerRequest.getParameter("password")).thenReturn(password);
        when(registerRequest.getParameter("email")).thenReturn("mvc@example.com");

        dispatcherServlet.service(registerRequest, registerResponse);

        verify(registerResponse).sendRedirect("/index.jsp");
        var savedUser = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertThat(savedUser.checkPassword(password)).isTrue();

        HttpSession session = mock(HttpSession.class);
        HttpServletRequest loginRequest = request("POST", "/login");
        HttpServletResponse loginResponse = mock(HttpServletResponse.class);
        when(loginRequest.getSession()).thenReturn(session);
        when(loginRequest.getParameter("account")).thenReturn(account);
        when(loginRequest.getParameter("password")).thenReturn(password);

        dispatcherServlet.service(loginRequest, loginResponse);

        verify(session).setAttribute(UserSession.SESSION_KEY, savedUser);
        verify(loginResponse).sendRedirect("/index.jsp");

        HttpServletRequest logoutRequest = request("GET", "/logout");
        HttpServletResponse logoutResponse = mock(HttpServletResponse.class);
        when(logoutRequest.getSession()).thenReturn(session);

        dispatcherServlet.service(logoutRequest, logoutResponse);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(logoutResponse).sendRedirect("/");
    }

    private HttpServletRequest request(String method, String uri) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }
}
