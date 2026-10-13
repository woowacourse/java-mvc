package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("samples");
        servlet.init();
    }

    @Test
    void rendersControllerFromConfiguredPackage() throws Exception {
        final var request = new MockHttpServletRequest("GET", "/get-test");
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(response.getForwardedUrl()).isEqualTo("/get-test.jsp");
    }

    @Test
    void sendsNotFoundWhenHandlerDoesNotExist() throws Exception {
        final var request = new MockHttpServletRequest("GET", "/missing");
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
        assertThat(response.isCommitted()).isTrue();
    }
}
