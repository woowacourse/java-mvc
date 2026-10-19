package com.interface21.webmvc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void 요청에_맞는_핸들러를_실행하고_뷰를_렌더링한다() throws Exception {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 요청을_처리할_핸들러가_없으면_404를_응답한다() throws Exception {
        when(request.getRequestURI()).thenReturn("/not-registered");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
