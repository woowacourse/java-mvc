package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.DispatcherServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.ModelAndView;

class UserControllerTest {

    @Test
    void 유저_정보를_반환한다() throws ServletException, IOException {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final ModelAndView modelAndView = mock(ModelAndView.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
