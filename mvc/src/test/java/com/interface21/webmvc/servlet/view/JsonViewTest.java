package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JsonViewTest {

    @Test
    void render_success_one_data() throws Exception {
        // given
        final Map<String, Object> model = Map.of("name", "pobi");

        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        final JsonView jsonView = new JsonView();

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(response.getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString())
                .isEqualTo("\"pobi\"");
    }

    @Test
    void render_success_two_data() throws Exception {
        // given
        final Map<String, Object> model = Map.of(
                "name", "pobi",
                "age", 20
        );

        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        final JsonView jsonView = new JsonView();
        final ObjectMapper objectMapper = new ObjectMapper();

        // when
        jsonView.render(model, request, response);
        final JsonNode actual = objectMapper.readTree(
                response.getContentAsString()
        );
        final JsonNode expected = objectMapper.readTree(
                "{\"name\":\"pobi\",\"age\":20}"
        );

        // then
        assertThat(response.getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void render_success_empty_data() throws Exception {
        // given
        final Map<String, Object> model = Map.of();

        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        final JsonView jsonView = new JsonView();

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(response.getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString())
                .isEqualTo("{}");
    }
}
