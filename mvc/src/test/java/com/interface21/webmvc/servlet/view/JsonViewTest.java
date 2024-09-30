package com.interface21.webmvc.servlet.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    JsonView jsonView = new JsonView();

    @DisplayName("Map을 json으로 변경한다.")
    @Test
    void render() throws Exception {
        Map<String, ?> model = Map.of(
                "name", "낙낙",
                "age", 20
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jsonView.render(model, request, response);

        String json = response.getContentAsString();
        assertThat(json).contains("\"name\":\"낙낙\"", "\"age\":20");
    }
}
