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
    void login_by_manualHandler() throws ServletException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/login");
        request.addParameter("account", "wrongAccount");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getRedirectedUrl()).isEqualTo("/401.jsp");
    }

    @Test
    void register_by_annotationHandler() throws ServletException {
        // given
        HttpServletRequest request = new MockHttpServletRequest("GET", "/register");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/register.jsp");
    }
}
