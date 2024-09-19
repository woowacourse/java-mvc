package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @Test
    @DisplayName("Redirect when the view name starts with 'redirect:' prefix.")
    void render_redirect() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var viewName = "redirect:index.jsp";

        var sut = new JspView(viewName);

        // when
        sut.render(Map.of(), request, response);

        // then
        assertThat(response.getRedirectedUrl()).isEqualTo("index.jsp");
    }

    @Test
    @DisplayName("Set request attributes from those of the model.")
    void render_set_request_attributes() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var model = Map.of(
                "account", "gugu",
                "password", "password");

        var sut = new JspView("foo.jsp");

        // when
        sut.render(model, request, response);

        // then
        assertThat(request.getAttribute("account")).isEqualTo("gugu");
        assertThat(request.getAttribute("password")).isEqualTo("password");
    }

    @Test
    @DisplayName("Forward to the resource matching the view name.")
    void render_forward() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var viewName = "index.jsp";
        var sut = new JspView(viewName);

        // when
        sut.render(Map.of(), request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("index.jsp");
    }
}
