package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void init() {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet("samples");

        // when & then
        assertThatCode(() -> dispatcherServlet.init())
                .doesNotThrowAnyException();
    }

    @Test
    void serviceWithAnnotationMvc() throws Exception {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.init();

        HttpServletRequest request = spy(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getAttribute("id")).thenReturn("mia");
        when(request.getRequestURI()).thenReturn("/test-annotation-mvc");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // when
        verify(requestDispatcher).forward(request, response);
    }
}
