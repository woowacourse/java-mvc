package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.View;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @DisplayName("리다이렉트 가능한 요청이면 리다이렉트 한다.")
    @Test
    void render1() throws Exception {
        View view = new JspView(JspView.REDIRECT_PREFIX + "/asd");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        view.render(Map.of(), request, response);

        assertThat(response.getRedirectedUrl()).isEqualTo("/asd");
    }

    @DisplayName("리다이렉트 가능한 요청이 아니면 요청에 model을 담고 포워딩한다.")
    @Test
    void render2() throws Exception {
        View view = new JspView("/asd");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        view.render(Map.of("name", "gugu"), request, response);

        assertThat(response.getForwardedUrl()).isEqualTo("/asd");
        assertThat(response.getRedirectedUrl()).isNull();
        assertThat(request.getAttribute("name")).isEqualTo("gugu");
    }


}
