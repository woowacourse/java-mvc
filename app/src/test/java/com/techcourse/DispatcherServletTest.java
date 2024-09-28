package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void execute_by_manualHandler() throws ServletException {
        // given
        HttpServletRequest request = new MockHttpServletRequest("GET", "/register");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getRedirectedUrl()).isEqualTo("/index.jsp");
    }

    @Test
    void execute_by_annotationHandler() throws ServletException {
        // given
        HttpServletRequest request = new MockHttpServletRequest("GET", "/register_annotation");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/register.jsp");
    }
}
