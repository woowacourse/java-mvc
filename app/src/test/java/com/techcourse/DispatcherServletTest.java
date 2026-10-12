package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.mvc.tobe.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.mockito.InOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("com.techcourse.controller");
        dispatcherServlet.init();
    }

    @ParameterizedTest
    @CsvSource({
        "/, /index.jsp",
        "/register, /register.jsp",
        "/login, /login.jsp"})
    void 화면_조회_요청을_해당_JSP로_전달한다(String uri, String viewName) throws Exception {
        // given
        final HttpServletRequest request = request("GET", uri);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service((ServletRequest) request, (ServletResponse) response);

        // then
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 로그인하면_사용자를_세션에_저장한다() throws Exception {
        // given
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest loginRequest = request("POST", "/login");
        final HttpServletResponse loginResponse = mock(HttpServletResponse.class);
        final User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();

        when(loginRequest.getSession()).thenReturn(session);
        when(loginRequest.getParameter("account")).thenReturn("gugu");
        when(loginRequest.getParameter("password")).thenReturn("password");

        // when
        dispatcherServlet.service((ServletRequest) loginRequest, (ServletResponse) loginResponse);

        // then
        verify(loginResponse).sendRedirect("/index.jsp");
        verify(session).setAttribute(UserSession.SESSION_KEY, user);
    }

    @Test
    void 로그아웃하면_세션의_사용자를_삭제한다() throws Exception {
        // given
        final HttpSession session = mock(HttpSession.class);
        final HttpServletRequest logoutRequest = request("POST", "/logout");
        final HttpServletResponse logoutResponse = mock(HttpServletResponse.class);

        when(logoutRequest.getSession()).thenReturn(session);

        // when
        dispatcherServlet.service((ServletRequest) logoutRequest, (ServletResponse) logoutResponse);

        // then
        verify(logoutResponse).sendRedirect("/");
        verify(session).removeAttribute(UserSession.SESSION_KEY);
    }

    @Test
    void 사용자_조회_결과를_JSON으로_응답한다() throws Exception {
        // given
        final HttpServletRequest request = request("GET", "/api/user");
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();

        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        // when
        dispatcherServlet.service((ServletRequest) request, (ServletResponse) response);

        // then
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
        final InOrder responseOrder = inOrder(response);
        responseOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        responseOrder.verify(response).getWriter();
    }

    private HttpServletRequest request(String method, String uri) {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);

        return request;
    }

}
