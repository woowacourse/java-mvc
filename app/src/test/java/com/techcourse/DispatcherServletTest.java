package com.techcourse;

import com.interface21.web.http.MediaType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void dispatchesLegacyAndAnnotationControllersThroughTheSameServlet() throws Exception {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest loginRequest = request("POST", "/login");
        final HttpSession session = mock(HttpSession.class);
        when(loginRequest.getSession()).thenReturn(session);
        when(loginRequest.getParameter("account")).thenReturn("missing-account");

        final HttpServletResponse loginResponse = mock(HttpServletResponse.class);
        dispatcherServlet.service(loginRequest, loginResponse);

        verify(loginResponse).sendRedirect("/401.jsp");

        final HttpServletRequest registerGetRequest = request("GET", "/register");
        final HttpServletResponse registerGetResponse = mock(HttpServletResponse.class);
        final RequestDispatcher registerView = mock(RequestDispatcher.class);
        when(registerGetRequest.getRequestDispatcher("/register.jsp")).thenReturn(registerView);

        dispatcherServlet.service(registerGetRequest, registerGetResponse);

        verify(registerView).forward(registerGetRequest, registerGetResponse);

        final HttpServletRequest registerViewRequest = request("GET", "/register/view");
        final HttpServletResponse registerViewResponse = mock(HttpServletResponse.class);
        final RequestDispatcher registerViewAlias = mock(RequestDispatcher.class);
        when(registerViewRequest.getRequestDispatcher("/register.jsp")).thenReturn(registerViewAlias);

        dispatcherServlet.service(registerViewRequest, registerViewResponse);

        verify(registerViewAlias).forward(registerViewRequest, registerViewResponse);

        final HttpServletRequest registerPostRequest = request("POST", "/register");
        when(registerPostRequest.getParameter("account")).thenReturn("review-feedback-test");
        when(registerPostRequest.getParameter("password")).thenReturn("password");
        when(registerPostRequest.getParameter("email")).thenReturn("review@example.com");

        final HttpServletResponse registerPostResponse = mock(HttpServletResponse.class);
        dispatcherServlet.service(registerPostRequest, registerPostResponse);

        verify(registerPostResponse).sendRedirect("/index.jsp");
    }

    @Test
    void rendersJsonForUserController() throws Exception {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest request = request("GET", "/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        dispatcherServlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    private HttpServletRequest request(final String method, final String requestURI) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getContextPath()).thenReturn("");
        return request;
    }
}
