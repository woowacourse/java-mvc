package com.interface21.webmvc.servlet.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JspViewTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void model_set_request_attribute() throws Exception {
        final JspView jspView = new JspView("/WEB-INF/views/hello.jsp");
        final Map<String, Object> model = Map.of("name", "John");

        jspView.render(model, request, response);

        assertThat(request.getAttribute("name")).isEqualTo("John");
    }

    @Test
    void redirect_except_path() throws Exception {
        final JspView jspView = new JspView("redirect:/home");

        jspView.render(Map.of(), request, response);

        assertThat(response.getRedirectedUrl()).isEqualTo("/home");
    }
}
