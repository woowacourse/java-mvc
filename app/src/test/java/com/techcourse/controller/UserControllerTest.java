package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcourse.framework.DispatcherServlet;
import com.techcourse.repository.InMemoryUserRepository;

class UserControllerTest {

    DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeEach
    void init() {
        dispatcherServlet.init();
    }

    @Test
    void test() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        dispatcherServlet.service(request, response);

        assertThat(stringWriter.toString()).isEqualTo(
                new ObjectMapper().writeValueAsString(
                        InMemoryUserRepository.findByAccount("gugu").get()
                ));
    }
}
