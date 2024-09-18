package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @Test
    @DisplayName("모델 값을 애트리뷰트로 전달한다.")
    void serve_model_value_to_http_request_attribute() throws Exception {
        // given
        final JspView jspView = new JspView("");
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // when
        jspView.render(Map.of("hello", "test"), request, response);

        // then
        assertThat(request.getAttribute("hello")).isSameAs("test");
    }

    @Test
    @DisplayName("redirect: prefix가 붙어있다면, 302 redirect한다.")
    void redirect_when_it_has_redirect_prefix() throws Exception {
        // given
        final JspView jspView = new JspView("redirect:/");
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // when
        jspView.render(Map.of(), request, response);

        // then
        assertThat(response.getStatus()).isEqualTo(302);
    }

    @Test
    @DisplayName("redirect: prefix가 붙어있다면, 그뒤 suffix path로 redirect한다.")
    void redirect_specific_path_when_it_has_redirect_prefix() throws Exception {
        // given
        final JspView jspView = new JspView("redirect:/index.html");
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = new MockHttpServletResponse();

        // when
        jspView.render(Map.of(), request, response);

        // then
        assertThat(response.getHeader("Location")).isEqualTo("/index.html");
    }
}
