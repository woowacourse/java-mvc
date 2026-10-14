package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    void 모델이_하나이면_객체를_JSON과_UTF_8로_응답한다() throws Exception {
        final JsonNode expected = objectMapper.readTree("""
                {
                    "account": "mingu",
                    "name": "밍구"
                }
                """);

        view.render(Map.of("user", new TestUser("mingu", "밍구")), request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(objectMapper.readTree(response.getContentAsByteArray()))
                .isEqualTo(expected);
    }

    @Test
    void 모델이_여러_개이면_키를_유지한_JSON_객체로_응답한다() throws Exception {
        final var model = Map.of("user", new TestUser("mingu", "밍구"), "count", 1);
        final JsonNode expected = objectMapper.readTree("""
                {
                    "user": {
                        "account": "mingu",
                        "name": "밍구"
                    },
                    "count": 1
                }
                """);

        view.render(model, request, response);

        assertThat(objectMapper.readTree(response.getContentAsByteArray()))
                .isEqualTo(expected);
    }

    @Test
    void 모델이_비어_있으면_빈_JSON_객체로_응답한다() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(response.getContentAsString()).isEqualTo("{}");
    }

    @Test
    void 모델의_유일한_값이_목록이면_JSON_배열로_응답한다() throws Exception {
        final JsonNode expected = objectMapper.readTree("""
                [
                    "밍구",
                    "구구"
                ]
                """);

        view.render(Map.of("users", List.of("밍구", "구구")), request, response);

        assertThat(objectMapper.readTree(response.getContentAsByteArray()))
                .isEqualTo(expected);
    }

    @Test
    void 모델의_유일한_값이_null이면_JSON_null로_응답한다() throws Exception {
        final var model = new HashMap<String, Object>();
        model.put("user", null);

        view.render(model, request, response);

        assertThat(response.getContentAsString()).isEqualTo("null");
    }

    public record TestUser(String account, String name) {
    }
}
