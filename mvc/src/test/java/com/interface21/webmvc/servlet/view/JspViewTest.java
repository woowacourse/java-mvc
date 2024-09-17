package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @DisplayName("viewName 이 redirect: 로 시작하면 리다이렉트 한다.")
    @Test
    void redirect() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JspView jspView = new JspView("redirect:");
        jspView.render(Map.of(), request, response);

        assertThat(response.getRedirectedUrl()).isEqualTo("");
    }

    @DisplayName("viewName 이 redirect: 로 시작하지 않으면 forward 한다.")
    @Test
    void forward() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JspView jspView = new JspView("/index.jsp");
        jspView.render(Map.of(), request, response);

        assertThat(response.getForwardedUrl()).isEqualTo("/index.jsp");
    }

    @DisplayName("model 에 들어있는 attribute 가 request 에 담긴다.")
    @Test
    void model() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JspView jspView = new JspView("/index.jsp");
        jspView.render(Map.of("name", "spring"), request, response);

        assertThat(request.getAttribute("name")).isEqualTo("spring");
    }
}
