package com.interface21.webmvc.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import testsupport.TestResponse;
import static org.mockito.Mockito.*;


import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("fixtures.mvc");
        servlet.init();
    }

    @Test
    void GET_핸들러를_실행하고_모델을_전달하여_forward한다() throws Exception {
        HttpServletRequest request = request("GET", "/fixture/page");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/fixture.jsp")).thenReturn(dispatcher);
        TestResponse response = new TestResponse();

        servlet.service(request, response.response);

        verify(dispatcher).forward(request, response.response);
        verify(request).setAttribute("message", "화면 데이터");
        verify(response.response, never()).sendRedirect(anyString());
    }

    @Test
    void 같은_URL의_POST_핸들러를_실행하고_redirect한다() throws Exception {
        HttpServletRequest request = request("POST", "/fixture/page");
        TestResponse response = new TestResponse();

        servlet.service(request, response.response);

        verify(response.response).sendRedirect("/fixture/page");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 요청_파라미터로_만든_모델을_JSON으로_응답한다() throws Exception {
        HttpServletRequest request = request("GET", "/fixture/json");
        when(request.getParameter("account")).thenReturn("gugu");
        TestResponse response = new TestResponse();

        servlet.service(request, response.response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(new ObjectMapper().readTree(response.getContentAsString()).asText())
                .isEqualTo("gugu");
        verify(request, never()).getRequestDispatcher(anyString());
        verify(response.response, never()).sendRedirect(anyString());
    }
    private HttpServletRequest request(String method, String uri) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }
}
