package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletIntegrationTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private Servlet servlet;
    private ServletRegistration.Dynamic registration;

    @BeforeEach
    void setUp() {
        final ServletContext servletContext = mock(ServletContext.class);
        registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(eq("dispatcher"), any(Servlet.class))).thenReturn(registration);

        new DispatcherServletInitializer().onStartup(servletContext);

        final ArgumentCaptor<Servlet> servletCaptor = ArgumentCaptor.forClass(Servlet.class);
        verify(servletContext).addServlet(eq("dispatcher"), servletCaptor.capture());
        servlet = servletCaptor.getValue();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void 초기화한_DispatcherServlet을_루트_경로에_등록한다() {
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
    }

    @Test
    void 홈_요청은_인덱스_JSP로_포워드한다() throws Exception {
        assertForward("/", "/index.jsp");
    }

    @Test
    void 비로그인_사용자에게_로그인_화면을_보여준다() throws Exception {
        assertForward("/login/view", "/login.jsp");
    }

    @Test
    void 로그인한_사용자의_로그인_화면_요청은_인덱스로_리다이렉트한다() throws Exception {
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1, "logged-in", "password", "user@example.com"));

        service("GET", "/login/view");

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 기존_회원가입_화면_경로를_유지한다() throws Exception {
        assertForward("/register/view", "/register.jsp");
    }

    @Test
    void 로그인에_성공하면_사용자를_세션에_저장하고_인덱스로_리다이렉트한다() throws Exception {
        final User user = givenUser();
        when(request.getParameter("password")).thenReturn("password");

        service("POST", "/login");

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 비밀번호가_틀리면_세션에_저장하지_않고_실패_화면으로_리다이렉트한다() throws Exception {
        final User user = givenUser();
        when(request.getParameter("password")).thenReturn("wrong-password");

        service("POST", "/login");

        verify(session, never()).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 존재하지_않는_계정으로_로그인하면_실패_화면으로_리다이렉트한다() throws Exception {
        when(request.getParameter("account")).thenReturn("missing-user-" + UUID.randomUUID());

        service("POST", "/login");

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 이미_로그인한_사용자는_다시_인증하지_않고_인덱스로_리다이렉트한다() throws Exception {
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1, "logged-in", "password", "user@example.com"));

        service("POST", "/login");

        verify(request, never()).getParameter("account");
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 로그아웃하면_세션의_사용자를_제거하고_홈으로_리다이렉트한다() throws Exception {
        service("GET", "/logout");

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    private User givenUser() {
        final String account = "mvc-login-test-" + UUID.randomUUID();
        final User user = new User(4, account, "password", "login@example.com");
        InMemoryUserRepository.save(user);
        when(request.getParameter("account")).thenReturn(account);
        return user;
    }

    private void assertForward(final String path, final String jspPath) throws Exception {
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(jspPath)).thenReturn(dispatcher);

        service("GET", path);

        verify(dispatcher).forward(request, response);
    }

    private void service(final String method, final String path) throws Exception {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);
        servlet.service(request, response);
    }
}
