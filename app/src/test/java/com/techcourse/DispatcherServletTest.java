package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("com.techcourse.controller");
        dispatcherServlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void register() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("dispatcher-user");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("user@example.com");

        dispatcherServlet.service((ServletRequest) request, (ServletResponse) response);

        final var user = InMemoryUserRepository.findByAccount("dispatcher-user").orElseThrow();
        assertThat(user.checkPassword("password")).isTrue();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void json() throws Exception {
        final var writer = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        dispatcherServlet.service((ServletRequest) request, (ServletResponse) response);

        assertThat(writer.toString()).isEqualTo("{\"account\":\"gugu\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void notFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/unknown");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service((ServletRequest) request, (ServletResponse) response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoMoreInteractions(response);
    }

    @Test
    void exceptionCause() {
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("unknown-user");

        assertThatThrownBy(() -> dispatcherServlet.service((ServletRequest) request, (ServletResponse) response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(NoSuchElementException.class);
    }
}
