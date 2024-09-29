package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    void register_by_annotationHandler() throws ServletException, IOException {
        // given
        HttpServletRequest request = new MockHttpServletRequest("GET", "/register/view");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/register.jsp");
    }
}
