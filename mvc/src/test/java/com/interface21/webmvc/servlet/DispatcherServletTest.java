package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void forwardToJsp() throws Exception {
        final var requestDispatcher = mock(RequestDispatcher.class);
        givenRequest("GET", "/forward-test");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void redirect() throws Exception {
        givenRequest("POST", "/redirect-test");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void renderJson() throws Exception {
        final var body = new StringWriter();
        givenRequest("GET", "/json-test");
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        dispatcherServlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("\"gugu\"");
    }

    @Test
    void throwExceptionWhenNoHandler() {
        givenRequest("GET", "/not-found");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("요청을 처리할 핸들러가 없습니다");
    }

    private void givenRequest(final String method, final String uri) {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
    }
}
