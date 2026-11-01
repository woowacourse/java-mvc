package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private TestDispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new TestDispatcherServlet();
        servlet.init();
    }

    @Test
    void 홈_컨트롤러가_반환한_JSP로_forward한다() throws Exception {
        final var request = createRequest("GET", "/");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void 어노테이션_로그아웃_처리_후_홈으로_redirect한다() throws Exception {
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
    void 어노테이션_로그인_화면을_조회한다() throws Exception {
        final var request = createRequest("GET", "/login/view");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void 로그인_화면에_POST로_요청하면_수동_매핑으로_넘어가지_않고_404를_반환한다() throws Exception {
        final var request = createRequest("POST", "/login/view");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
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
    void 컨텍스트_경로가_있어도_홈_컨트롤러를_찾는다() throws Exception {
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

    @Test
    void 사용자_조회는_비밀번호_없이_JSON을_반환한다() throws Exception {
        final var request = createRequest("GET", "/api/user");
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
        assertThat(output.toString()).doesNotContain("password");
    }

    @Test
    void 없는_사용자를_조회하면_404와_JSON_오류를_반환한다() throws Exception {
        final var request = createRequest("GET", "/api/user");
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(request.getParameter("account")).thenReturn("missing-step3-user");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertThat(output.toString()).contains("사용자를 찾을 수 없습니다.");
    }

    @Test
    void 계정_없이_사용자를_조회하면_400을_반환한다() throws Exception {
        final var request = createRequest("GET", "/api/user");
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void 올바른_비밀번호로_로그인하면_세션을_저장하고_redirect한다() throws Exception {
        final var request = createRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY,
                InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_실패_화면으로_redirect한다() throws Exception {
        final var request = createRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    private HttpServletRequest createRequest(final String method, final String requestURI) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getContextPath()).thenReturn("");
        return request;
    }

    private static class TestDispatcherServlet extends DispatcherServlet {

        TestDispatcherServlet() {
            super("com.techcourse.controller");
        }

        @Override
        public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
            super.service(request, response);
        }
    }

}
