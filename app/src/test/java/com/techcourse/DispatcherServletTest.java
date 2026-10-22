package com.techcourse;

import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
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
    void 등록되지_않은_요청이면_404를_반환한다() throws Exception {
        final var request = createRequest("GET", "/unknown");
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
