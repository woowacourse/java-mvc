package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("viewName 앞에 redirect:가 같이 있다면 리다이렉트된다.")
    void redirect() throws Exception {
        JspView jspView = new JspView("redirect:/index");

        jspView.render(null, request, response);

        assertThat(response.containsHeader("Location")).isTrue();
    }
}
