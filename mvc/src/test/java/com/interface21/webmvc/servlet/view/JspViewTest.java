package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @Test
    void render() throws Exception {
        // given
        JspView jspView = new JspView("");
        String attributeName = "name";
        String attributeValue = "kirby";
        Map<String, ?> model = Map.of(attributeName, attributeValue);
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        // when
        jspView.render(model, request, response);

        // then
        Object actual = request.getAttribute(attributeName);
        assertThat(actual).isEqualTo(attributeValue);
    }
}
