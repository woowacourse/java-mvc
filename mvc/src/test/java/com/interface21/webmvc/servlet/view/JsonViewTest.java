package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void writer를_얻기_전에_JSON_UTF8_콘텐츠_타입을_설정한다() throws Exception {
        render(Map.of());

        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
    }

    @Test
    void 빈_모델은_빈_JSON_객체로_응답한다() throws Exception {
        final var body = render(Map.of());

        assertThat(body).isEqualTo("{}");
    }

    @Test
    void 모델이_하나면_값을_직접_JSON으로_응답한다() throws Exception {
        final var body = render(Map.of("user", new User("포키", List.of("개발", "독서"))));
        final var json = objectMapper.readTree(body);

        assertThat(json.size()).isEqualTo(2);
        assertThat(json.get("name").asText()).isEqualTo("포키");
        assertThat(json.get("interests").get(0).asText()).isEqualTo("개발");
        assertThat(json.get("interests").get(1).asText()).isEqualTo("독서");
    }

    @Test
    void 모델이_여러_개면_키와_값을_모두_JSON으로_응답한다() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", new User("포키", List.of("개발")));
        model.put("count", 1);
        model.put("next", null);

        final var body = render(model);
        final var json = objectMapper.readTree(body);

        assertThat(json.size()).isEqualTo(3);
        assertThat(json.get("user").get("name").asText()).isEqualTo("포키");
        assertThat(json.get("count").asInt()).isEqualTo(1);
        assertThat(json.get("next").isNull()).isTrue();
    }

    @Test
    void 한글과_특수문자를_JSON으로_응답한다() throws Exception {
        final String message = "안녕 \"포키\"\nC:\\mvc";

        final var body = render(Map.of("message", message));

        assertThat(body).isEqualTo("\"안녕 \\\"포키\\\"\\nC:\\\\mvc\"");
        assertThat(objectMapper.readTree(body).asText()).isEqualTo(message);
    }

    @Test
    void 유일한_모델의_값이_null이면_JSON_null로_응답한다() throws Exception {
        final var body = render(Collections.singletonMap("user", null));

        assertThat(body).isEqualTo("null");
    }

    private String render(final Map<String, ?> model) throws Exception {
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        view.render(model, mock(HttpServletRequest.class), response);
        return output.toString();
    }

    record User(String name, List<String> interests) {
    }
}
