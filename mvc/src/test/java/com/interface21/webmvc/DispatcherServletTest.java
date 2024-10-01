package com.interface21.webmvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void init() {
        assertDoesNotThrow(() -> new DispatcherServlet().init());
    }

    @DisplayName("등록하지 않은 URL 요청 시 404 응답")
    @Test
    void serviceRequestInvalidUrl() throws Exception {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        Response response = new Response();
        response.setCoyoteResponse(new org.apache.coyote.Response());
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/invalid");

        dispatcherServlet.service(request, response);

        Assertions.assertEquals(404, response.getStatus());
    }
}
