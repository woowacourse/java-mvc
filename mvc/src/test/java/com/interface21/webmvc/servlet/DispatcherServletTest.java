package com.interface21.webmvc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DispatcherServletTest {

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

    @DisplayName("/api/user 요청 시 유저 정보를 Json 타입으로 반환한다.")
    @Test
    void apiUserTest() throws ServletException, IOException {

        // given
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        dispatcherServlet.service(request, response);

        // then
        String expected = """
                {"id":1,"account":"gugu","password":"password","email":"hkkang@woowahan.com"}""";
        verify(writer).write(expected);
    }
}
