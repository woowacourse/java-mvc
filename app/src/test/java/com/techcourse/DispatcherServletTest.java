package com.techcourse;

import com.interface21.web.http.MediaType;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    void 컨트롤러가_JSP_뷰_이름을_반환하면_포워드한다() throws Exception {
        final var request = createRequest("GET", "/");
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 애노테이션_컨트롤러로_회원가입_화면을_보여준다() throws Exception {
        final var request = createRequest("GET", "/register");
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 애노테이션_컨트롤러로_회원가입한다() throws Exception {
        final var request = createRequest("POST", "/register");
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("woody");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("woody@email.com");

        dispatcherServlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount("woody")).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 애노테이션_컨트롤러로_로그인에_성공하면_세션에_사용자를_저장하고_리다이렉트한다() throws Exception {
        final var request = createRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        dispatcherServlet.service(request, response);

        verify(session).setAttribute(eq(UserSession.SESSION_KEY), any(User.class));
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 애노테이션_컨트롤러로_로그인할_때_비밀번호가_틀리면_401_페이지로_리다이렉트한다() throws Exception {
        final var request = createRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 애노테이션_컨트롤러로_로그인할_때_존재하지_않는_계정이면_401_페이지로_리다이렉트한다() throws Exception {
        final var request = createRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("unknown");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 이미_로그인한_상태로_로그인하면_메인_페이지로_리다이렉트한다() throws Exception {
        final var request = createRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(new User(1, "gugu", "password", "gugu@email.com"));

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 컨트롤러가_redirect_뷰_이름을_반환하면_리다이렉트한다() throws Exception {
        final var request = createRequest("GET", "/logout");
        final var response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void 애노테이션_컨트롤러로_사용자_정보를_JSON으로_응답한다() throws Exception {
        final var request = createRequest("GET", "/api/user");
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        dispatcherServlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void 처리할_핸들러가_없으면_404를_응답한다() throws Exception {
        final var request = createRequest("GET", "/not-found");
        final var response = mock(HttpServletResponse.class);

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private HttpServletRequest createRequest(String method, String requestURI) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn(requestURI);
        return request;
    }
}
