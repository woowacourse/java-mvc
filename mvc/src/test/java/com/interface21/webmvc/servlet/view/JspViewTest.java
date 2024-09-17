package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @DisplayName("response를 viewName으로 forward한다.")
    @Test
    void render() throws Exception {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String expectedUrl = "index.jsp";
        JspView view = new JspView(expectedUrl);

        // when
        view.render(new HashMap<>(), request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo(expectedUrl);
    }

    @DisplayName("viewName에 redirect:가 포함되면, 헤더에 location을 추가하고 302 상태코드를 반환한다.")
    @Test
    void render_withRedirect() throws Exception {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        JspView view = new JspView("redirect:index.jsp");

        // when
        view.render(new HashMap<>(), request, response);

        // then
        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(response.containsHeader("location")).isTrue();
    }

    @DisplayName("model을 request attribute로 추가한다.")
    @Test
    void render_setAttributeByModel() throws Exception {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        JspView view = new JspView("redirect:index.jsp");
        String name = "atom";
        int age = 25;

        // when
        view.render(Map.of("name", name, "age", age), request, response);

        // then
        assertThat(request.getAttribute("name")).isEqualTo(name);
        assertThat(request.getAttribute("age")).isEqualTo(age);
    }
}