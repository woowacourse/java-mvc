package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Test
    @DisplayName("로그인 화면 요청은 login.jsp로 포워딩한다")
    void loginViewRequestForwardsToLoginJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("이미 로그인한 사용자의 로그인 화면 요청은 메인 화면으로 리다이렉트한다")
    void authenticatedUserLoginViewRequestRedirectsToIndex() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1, "gugu", "password", "gugu@example.com"));

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("로그인 요청은 어노테이션 매핑으로 인증하고 메인 화면으로 리다이렉트한다")
    void loginRequestAuthenticatesAndRedirectsToIndex() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        verify(session).setAttribute(eq(UserSession.SESSION_KEY), any());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("로그인 화면 경로는 어노테이션 매핑에 등록한다")
    void loginViewPathIsRegisteredInAnnotationMapping() {
        assertAnnotationHandlerRegistered("/login", "GET");
    }

    @Test
    @DisplayName("로그인 경로는 어노테이션 매핑에 등록한다")
    void loginPathIsRegisteredInAnnotationMapping() {
        assertAnnotationHandlerRegistered("/login", "POST");
    }

    @Test
    @DisplayName("회원가입 화면 요청은 register.jsp로 포워딩한다")
    void registerPageRequestForwardsToRegisterJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("회원가입 요청은 사용자를 저장하고 메인 화면으로 리다이렉트한다")
    void registerRequestSavesUserAndRedirectsToIndex() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String account = "dispatcher-test-" + UUID.randomUUID();
        String password = "password";

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn("dispatcher-test@example.com");

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account))
                .hasValueSatisfying(user -> assertThat(user.checkPassword(password)).isTrue());
        verify(response).sendRedirect("/index.jsp");
    }

    private void assertAnnotationHandlerRegistered(String requestUri, String method) {
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(requestUri);
        when(request.getMethod()).thenReturn(method);

        assertThat(handlerMapping.getHandler(request)).isNotNull();
    }
}
