package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

class JspViewTest {

    @DisplayName("redirect: 로 시작하는 view의 경우 응답 location에 view가 저장된다.")
    @Test
    void givenStartsWithRedirect_whenRender_thenLocationSameAsView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();

        JspView jspView = new JspView("redirect:/hello");
        jspView.render(Map.of(), request, response);

        assertThat(response.getHeader("Location")).isEqualTo("/hello");
    }

    @DisplayName("redirect: 로 시작하는 view의 경우 응답 location에 view가 저장된다.")
    @Test
    void givenStartsWithoutRedirect_whenRender_thenForwardedUrlSameAsView() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JspView jspView = new JspView("/hello");
        jspView.render(Map.of(), request, response);

        assertAll(
                () -> assertThat(response.getHeader("Location")).isNull(),
                () -> assertThat(response.getForwardedUrl()).isEqualTo("/hello")
        );
    }
}
