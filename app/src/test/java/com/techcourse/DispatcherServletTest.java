package com.techcourse;

import com.interface21.web.http.MediaType;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

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
    @DisplayName("홈 요청은 index.jsp로 전달한다")
    void showsHomeView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("POST 로그인에 성공하면 사용자를 세션에 저장하고 홈으로 이동한다")
    void logsInWithPostRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(null);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        dispatcherServlet.service(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY,
                InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("비밀번호가 틀리면 오류 화면으로 이동한다")
    void redirectsOnInvalidPassword() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    @DisplayName("GET /login은 POST 로그인 핸들러를 실행하지 않는다")
    void doesNotLogInWithGetRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인하지 않은 사용자가 로그인 화면을 요청하면 JSP를 보여준다")
    void showsLoginViewForGuest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("로그인한 사용자가 로그인 화면을 요청하면 홈으로 이동한다")
    void redirectsLoggedInUserFromLoginView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("GET 로그아웃은 세션의 사용자 속성을 제거하고 홈으로 이동한다")
    void logsOutWithGetRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("같은 URL의 GET 요청은 애노테이션 컨트롤러의 화면 메서드를 실행한다")
    void runsGetHandlerForRegister() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("회원가입 화면 요청은 register.jsp로 전달한다")
    void showsRegisterView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("컨트롤러의 모델을 JSP 요청 속성으로 전달한다")
    void passesModelToView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/model-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/model-test.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("message", "hello");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("같은 URL의 POST 요청은 애노테이션 컨트롤러의 저장 메서드를 실행한다")
    void runsPostHandlerForRegister() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var account = "new-user-" + UUID.randomUUID();

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("new-user@example.com");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount(account))
                .hasValueSatisfying(user -> assertThat(user.checkPassword("password")).isTrue());
    }

    @Test
    @DisplayName("사용자 조회 요청은 계정을 JSON으로 응답한다")
    void returnsUserAsJson() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        dispatcherServlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    @DisplayName("등록되지 않은 URL은 404 응답으로 처리한다")
    void returnsNotFoundForUnknownUrl() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/unknown");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
