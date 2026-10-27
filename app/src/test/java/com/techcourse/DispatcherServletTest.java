package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Test
    void 기존_컨트롤러가_반환한_JSP로_forward한다() throws Exception {
        final var request = createRequest("GET", "/");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void 기존_로그아웃_처리_후_홈으로_redirect한다() throws Exception {
        final var request = createRequest("GET", "/logout");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void 어노테이션_컨트롤러로_회원가입_화면을_조회한다() throws Exception {
        final var request = createRequest("GET", "/register");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void 어노테이션_컨트롤러로_회원을_저장한_뒤_redirect한다() throws Exception {
        final var request = createRequest("POST", "/register");
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("step2-user");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("step2@example.com");

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount("step2-user").orElseThrow();
        assertThat(user.checkPassword("password")).isTrue();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 기존_로그인_화면도_함께_동작한다() throws Exception {
        final var request = createRequest("GET", "/login/view");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void 같은_URL과_메서드를_두_매핑이_모두_처리하면_어노테이션_매핑이_선택된다() throws Exception {
        final var request = createRequest("POST", "/login/view");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/annotation-login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(request, never()).getRequestDispatcher("/login.jsp");
    }

    @Test
    void 어노테이션_매핑에_해당_메서드가_없으면_수동_매핑으로_넘어간다() throws Exception {
        final var request = createRequest("GET", "/login/view");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(request, never()).getRequestDispatcher("/annotation-login.jsp");
    }

    @Test
    void 등록되지_않은_요청이면_404를_반환한다() throws Exception {
        final var request = createRequest("GET", "/unknown");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 회원가입_URL에_매핑되지_않은_HTTP_메서드로_요청하면_404를_반환한다() throws Exception {
        final var request = createRequest("DELETE", "/register");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 컨텍스트_경로가_있어도_기존_컨트롤러를_찾는다() throws Exception {
        final var request = createRequest("GET", "/mvc/");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void 컨텍스트_경로가_있어도_어노테이션_컨트롤러를_찾는다() throws Exception {
        final var request = createRequest("GET", "/mvc/register");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void 어노테이션_컨트롤러의_모델을_뷰에_전달한다() throws Exception {
        final var request = createRequest("GET", "/model-test");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getParameter("id")).thenReturn("gugu");
        when(request.getRequestDispatcher("/model.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(dispatcher).forward(request, response);
    }

    @Test
    void 컨트롤러_실행_실패의_원인을_보존한다() {
        final var request = createRequest("GET", "/failing-test");
        final var response = mock(HttpServletResponse.class);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("컨트롤러 실행 실패");
    }

    private HttpServletRequest createRequest(final String method, final String requestURI) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getContextPath()).thenReturn("");
        return request;
    }
}
