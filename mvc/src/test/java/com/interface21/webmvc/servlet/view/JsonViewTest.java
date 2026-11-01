package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 단일_모델은_키를_벗기고_값을_JSON으로_반환한다() throws Exception {
        final String json = render(Map.of("user", Map.of("account", "티뉴\"\n")));

        assertThat(objectMapper.readTree(json).get("account").asText()).isEqualTo("티뉴\"\n");
    }

    @Test
    void 모델이_여러_개면_모든_키를_유지한다() throws Exception {
        final var model = Map.of("account", "tinu", "count", 2);

        assertThat(objectMapper.readTree(render(model))).isEqualTo(objectMapper.valueToTree(model));
    }

    @Test
    void 빈_모델은_빈_JSON_객체를_반환한다() throws Exception {
        assertThat(render(Map.of())).isEqualTo("{}");
    }

    @Test
    void 단일_모델의_null_값도_반환한다() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", null);

        assertThat(render(model)).isEqualTo("null");
    }

    @Test
    void 직렬화에_실패하면_응답을_건드리지_않고_원인을_전달한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var model = Map.of("value", new UnserializableValue());

        assertThatThrownBy(() -> new JsonView().render(model, request, response))
                .isInstanceOf(JsonProcessingException.class)
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("직렬화 실패");
        verifyNoInteractions(request, response);
    }

    private String render(final Map<String, ?> model) throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        new JsonView().render(model, request, response);

        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        verifyNoInteractions(request);
        return output.toString();
    }

    private static class UnserializableValue {

        public String getValue() {
            throw new IllegalStateException("직렬화 실패");
        }
    }
}
