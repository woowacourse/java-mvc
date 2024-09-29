package com.interface21.webmvc.servlet.mvc.tobe;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.DispatcherServlet;

public class DispatcherServletTest {

    private DispatcherServlet sut;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        sut = new DispatcherServlet("samples");
        sut.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Nested
    @DisplayName("Controller 어노테이션 기반 컨트롤러에 대한 요청을 처리한다.")
    class ControllerAnnotation {

        @Test
        @DisplayName("/register에 대한 GET 요청을 처리한다.")
        void handleRegisterGET() throws ServletException, IOException {
            // given
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            // when
            sut.service(request, response);

            // then
            verify(requestDispatcher).forward(request, response);
        }

        @Test
        @DisplayName("/register에 대한 POST 요청을 처리한다.")
        void handleRegisterPOST() throws ServletException, IOException {
            // given
            final var requestDispatcher = mock(RequestDispatcher.class);

            when(request.getParameter("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");
            when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

            // when
            sut.service(request, response);

            // then
            verify(requestDispatcher).forward(request, response);
        }
    }
}
