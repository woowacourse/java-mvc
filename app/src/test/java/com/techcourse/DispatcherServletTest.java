package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("com.techcourse");
        dispatcherServlet.init();
        response = mock(HttpServletResponse.class);
    }

    @Test
    void 홈_요청을_forward한다() throws Exception {
        final var request = request("GET", "/");
        final var requestDispatcher = requestDispatcher(request, "/index.jsp");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 어노테이션_컨트롤러가_회원가입_화면을_forward한다() throws Exception {
        final var request = request("GET", "/register");
        final var requestDispatcher = requestDispatcher(request, "/register.jsp");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 같은_URL이라도_HTTP_메서드에_맞는_어노테이션_핸들러가_처리한다() throws Exception {
        final var request = request("POST", "/register");
        when(request.getParameter("account")).thenReturn("dispatcher-test");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("dispatcher-test@example.com");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount("dispatcher-test")).isPresent();
    }

    @Test
    void 어느_매핑에도_없는_요청이면_예외가_발생한다() {
        final var request = request("GET", "/not-found");

        assertThatThrownBy(() -> dispatcherServlet.service((ServletRequest) request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("GET /not-found");
    }

    @Test
    void 로그인에_성공하면_사용자를_세션에_저장한다() throws Exception {
        final var request = request("POST", "/login");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(session).setAttribute("user", InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 비밀번호가_틀리면_로그인_실패_화면으로_이동한다() throws Exception {
        final var request = request("POST", "/login");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 로그인하지_않았으면_로그인_화면을_forward한다() throws Exception {
        final var request = request("GET", "/login/view");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        final var requestDispatcher = requestDispatcher(request, "/login.jsp");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 로그인한_사용자는_로그인_화면에서_홈으로_이동한다() throws Exception {
        final var request = request("GET", "/login/view");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 로그아웃하면_세션의_사용자를_제거하고_홈으로_이동한다() throws Exception {
        final var request = request("GET", "/logout");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(session).removeAttribute("user");
        verify(response).sendRedirect("/");
    }

    @Test
    void 회원가입_화면의_기존_URL을_유지한다() throws Exception {
        final var request = request("GET", "/register/view");
        final var requestDispatcher = requestDispatcher(request, "/register.jsp");

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 사용자_조회_API는_모델_키_없이_사용자를_JSON으로_응답한다() throws Exception {
        final var request = request("GET", "/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    private HttpServletRequest request(final String method, final String uri) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getContextPath()).thenReturn("");
        return request;
    }

    private RequestDispatcher requestDispatcher(final HttpServletRequest request, final String path) {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(path)).thenReturn(requestDispatcher);
        return requestDispatcher;
    }
}
