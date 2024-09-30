package com.techcourse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    @DisplayName("처리 불가능한 요청이 온다면 404 페이지를 반환한다.")
    void handleError() throws IOException {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/coduo");
        when(request.getMethod()).thenReturn("GET");

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        //when
        dispatcherServlet.service(request, response);

        //then
        verify(response).sendRedirect("/404.jsp");
    }
}
