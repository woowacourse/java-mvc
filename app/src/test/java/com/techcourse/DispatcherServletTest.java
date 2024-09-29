package com.techcourse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void init() {
        assertDoesNotThrow(() -> new DispatcherServlet().init());
    }

    @DisplayName("등록하지 않은 URL 요청 시 404 응답")
    @Test
    void serviceRequestInvalidUrl() throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        Response response = new Response();
        response.setCoyoteResponse(new org.apache.coyote.Response());
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/invalid");

        dispatcherServlet.service(request, response);

        assertEquals(404, response.getStatus());
    }

    @DisplayName("/ URL 요청 시 index.jsp 200 응답")
    @Test
    void serviceRequestRoot() throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        Response response = new Response();
        response.setCoyoteResponse(new org.apache.coyote.Response());
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(mock(RequestDispatcher.class));

        dispatcherServlet.service(request, response);

        assertEquals(200, response.getStatus());
    }
}
