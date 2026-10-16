package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    @DisplayName("모델이 하나면 키를 제외하고 값만 JSON으로 응답한다")
    void rendersSingleValue() throws Exception {
        final String body = render(Map.of("user", Map.of("account", "gugu")));

        assertThat(body).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    @DisplayName("모델이 여러 개면 전체 Map을 JSON으로 응답한다")
    void rendersEntireModel() throws Exception {
        final var body = new ObjectMapper().readTree(render(Map.of("account", "gugu", "count", 2)));

        assertThat(body.size()).isEqualTo(2);
        assertThat(body.get("account").asText()).isEqualTo("gugu");
        assertThat(body.get("count").asInt()).isEqualTo(2);
    }

    @Test
    @DisplayName("빈 모델은 빈 JSON 객체로 응답한다")
    void rendersEmptyModel() throws Exception {
        assertThat(render(Map.of())).isEqualTo("{}");
    }

    @Test
    @DisplayName("하나뿐인 모델 값이 null이면 JSON null로 응답한다")
    void rendersSingleNullValue() throws Exception {
        final Map<String, Object> model = new HashMap<>();
        model.put("user", null);

        assertThat(render(model)).isEqualTo("null");
    }

    @Test
    @DisplayName("한글과 특수문자를 JSON UTF-8 응답으로 전달한다")
    void rendersUnicodeWithJsonContentType() throws Exception {
        final String message = "재키의 \"MVC\"\n완료 😀";
        final var body = new ObjectMapper().readTree(render(Map.of("message", message)));

        assertThat(body.asText()).isEqualTo(message);
        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
    }

    private String render(final Map<String, ?> model) throws Exception {
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        view.render(model, mock(HttpServletRequest.class), response);
        return body.toString();
    }
}
