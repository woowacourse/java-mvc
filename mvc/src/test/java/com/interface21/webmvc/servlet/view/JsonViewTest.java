package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.io.PrintWriter;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void unwrapsSingleModelValue() throws Exception {
        final var response = render(Map.of("user", new SampleUser("레서")));

        assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree("{\"account\":\"레서\"}"));
    }

    @Test
    void rendersMultipleEntriesAsObject() throws Exception {
        final var model = Map.of("user", new SampleUser("레서"), "count", 1);

        assertThat(objectMapper.readTree(render(model)))
                .isEqualTo(objectMapper.valueToTree(model));
    }

    @Test
    void rendersEmptyModelAsEmptyObject() throws Exception {
        assertThat(render(Map.of())).isEqualTo("{}");
    }

    @Test
    void rendersSingleNullValue() throws Exception {
        assertThat(render(Collections.singletonMap("user", null))).isEqualTo("null");
    }

    @Test
    void rendersSingleCollectionAsArray() throws Exception {
        final var response = render(Map.of("users", List.of("a", "b")));

        assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree("[\"a\",\"b\"]"));
    }

    @Test
    void setsUtf8ContentTypeBeforeObtainingWriter() throws Exception {
        assertThat(render(Map.of("message", "안녕하세요"))).isEqualTo("\"안녕하세요\"");
    }

    private String render(Map<String, ?> model) throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        new JsonView().render(model, mock(HttpServletRequest.class), response);

        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        return output.toString();
    }

    public record SampleUser(String account) {
    }
}
