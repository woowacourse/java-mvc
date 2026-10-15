package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.domain.User;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private final DispatcherServlet servlet = new DispatcherServlet();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeEach
    void setUp() {
        servlet.init();
    }

    @Test
    void annotationControllerShowsRegistrationForm() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationControllerSavesUserAndRedirects() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("registered-user");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("user@example.com");

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount("registered-user")).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void legacyControllerStillShowsRegistrationForm() throws Exception {
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void returnsUserAsJson() throws Exception {
        InMemoryUserRepository.save(new User(3, "json-user", "password", "user@example.com"));
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("json-user");
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.service(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(body.toString()).isEqualTo("{\"account\":\"json-user\"}");
    }

    @Test
    void unmappedRequestReturnsNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
