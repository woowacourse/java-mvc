package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void 홈_요청을_처리하고_시작_JSP로_전달한다() throws Exception {
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
    void 로그아웃_요청을_처리하고_홈_주소로_이동한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void 어노테이션_GET_요청의_모델을_JSP로_전달한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("id")).thenReturn("gugu");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 같은_URL의_POST_요청은_다른_어노테이션_메서드로_처리한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("POST");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/annotation-test");
    }

    @Test
    void 같은_URL이_등록되어_있으면_어노테이션_컨트롤러를_우선한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/annotation-login.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 등록되지_않은_요청에는_404로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/unknown");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 컨트롤러_실행_실패의_원인을_보존한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/annotation-error");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("요청 처리 실패");
    }
}
