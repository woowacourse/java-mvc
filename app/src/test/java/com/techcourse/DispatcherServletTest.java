package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.techcourse.repository.InMemoryUserRepository;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("POST /register 요청을 보내면 Annotation 기반 RegisterController가 동작한다.")
    @Test
    void mapControllerByAnnotation() throws ServletException {
        // given
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getParameter("account")).thenReturn("kargo");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("kargo@email.com");

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(InMemoryUserRepository.findByAccount("kargo")).isNotEmpty();
    }
}
