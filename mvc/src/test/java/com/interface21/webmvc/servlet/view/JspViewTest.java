package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @Test
    @DisplayName("view name 이 'redirect:' 로 시작하는 경우 리다이렉트 한다.")
    void redirectTest() throws Exception {
        String viewName = "redirect:/test";
        JspView jspView = new JspView(viewName);
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        jspView.render(Map.of(), request, response);

        assertAll(
                () -> assertThat(response.getRedirectedUrl()).isEqualTo("/test"),
                () -> assertThat(response.getStatus()).isEqualTo(302)
        );
    }

    @Test
    @DisplayName("view name 이 'redirect:' 로 시작하지 않는 경우 forward 한다.")
    void forwardTest() throws Exception {
        String viewName = "/test.jsp";
        JspView jspView = new JspView(viewName);
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        jspView.render(Map.of(), request, response);

        assertThat(response.getForwardedUrl()).isEqualTo("/test.jsp");
    }
}
