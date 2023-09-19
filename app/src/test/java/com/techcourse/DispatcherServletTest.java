package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    public static class TestRequestDispatcher implements RequestDispatcher {

        public TestRequestDispatcher() {
        }

        @Override
        public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        }

        @Override
        public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        }
    }

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void serviceWithClassHandler() throws ServletException {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getRequestDispatcher("/register.jsp"))
                .thenReturn(new TestRequestDispatcher());

        dispatcherServlet.service(request, response);

        verify(request, times(1)).getRequestDispatcher("/register.jsp");
    }

    @Test
    void serviceWithAnnotatedHandler() throws ServletException {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp"))
                .thenReturn(new TestRequestDispatcher());

        dispatcherServlet.service(request, response);

        verify(request, times(1)).getRequestDispatcher("/register.jsp");
    }

    @Test
    void notFindAppropriateHandler() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/false");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("Failed to find appropriate handler for this request.");
    }
}

