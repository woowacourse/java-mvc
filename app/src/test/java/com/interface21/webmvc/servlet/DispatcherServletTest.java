package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.RequestDispatcher;
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
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("com.techcourse");
        dispatcherServlet.init();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    void forwardsRegisterView() throws Exception {
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void forwardsAnnotationHandlerResultWithModel() throws Exception {
        when(request.getRequestURI()).thenReturn("/dispatcher-test");
        when(request.getRequestDispatcher("/dispatcher-test.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void rendersUserAsJsonThroughAnnotationHandler() throws Exception {
        final var responseBody = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        dispatcherServlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(responseBody.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }
}
