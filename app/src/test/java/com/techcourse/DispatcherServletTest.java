package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @DisplayName("올바른 uri 요청을 보내면 요청을 처리한다.")
    @Test
    void service1() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(request.getRequestURI()).willReturn("/register");
        given(request.getMethod()).willReturn("GET");

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/register.jsp");
    }

    @DisplayName("올바르지 uri 요청을 보내면 에러를 던진다.")
    @Test
    void service2() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(request.getRequestURI()).willReturn("/wrong");
        given(request.getMethod()).willReturn("GET");

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        assertThatCode(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
