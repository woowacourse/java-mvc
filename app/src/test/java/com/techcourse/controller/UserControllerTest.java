package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));

        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        dispatcherServlet.init();
    }

    @Test
    void show() throws ServletException, IOException {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("woohyeon");

        when(response.getWriter()).thenReturn(new PrintWriter(body, true));

        InMemoryUserRepository.save(
                new User(1L, "woohyeon", "password", "woohyeon@example.com"));

        //when
        dispatcherServlet.service(request, response);

        //then
        verify(response).setContentType(startsWith("application/json"));
        assertThat(body.toString()).contains("\"account\":\"woohyeon\"");
    }
}
