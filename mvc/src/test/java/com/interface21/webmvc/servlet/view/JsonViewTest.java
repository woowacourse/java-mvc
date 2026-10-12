package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void preservesUserEnvelopeWhenMetadataIsAdded() throws Exception {
        final var user = new SampleResponse("gugu");
        final var single = render(new JsonView(), Map.of("user", user));
        final var multiple = render(new JsonView(), Map.of("user", user, "message", "ok"));

        assertThat(single.path("user").path("account").asText()).isEqualTo("gugu");
        assertThat(multiple.path("user").path("account").asText()).isEqualTo("gugu");
        assertThat(single.has("account")).isFalse();
        assertThat(multiple.path("message").asText()).isEqualTo("ok");
    }

    @Test
    void rendersEmptyModelAsJsonObject() throws Exception {
        assertThat(render(new JsonView(), Map.of())).isEqualTo(objectMapper.readTree("{}"));
    }

    @Test
    void preservesNestedCollectionsAndNullValues() throws Exception {
        final Map<String, Object> model = new HashMap<>();
        model.put("user", null);
        model.put("values", Arrays.asList(Map.of("number", 1), null));

        assertThat(render(new JsonView(), model))
                .isEqualTo(objectMapper.readTree("{\"user\":null,\"values\":[{\"number\":1},null]}"));
    }

    @Test
    void escapesUnicodeQuotesBackslashesAndNewlines() throws Exception {
        final String account = "구구\"\\\n";
        final var json = render(new JsonView(), Map.of("user", new SampleResponse(account)));
        assertThat(json.path("user").path("account").asText()).isEqualTo(account);
    }

    @Test
    void setsJsonEncodingBeforeObtainingWriter() throws Exception {
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        new JsonView().render(Map.of(), mock(HttpServletRequest.class), response);

        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).setCharacterEncoding(StandardCharsets.UTF_8);
        order.verify(response).getWriter();
    }

    @Test
    void keepsDifferentViewResponsesIndependent() throws Exception {
        assertThat(render(new JsonView(), Map.of("user", new SampleResponse("first")))
                .path("user").path("account").asText()).isEqualTo("first");
        assertThat(render(new JsonView(), Map.of("user", new SampleResponse("second")))
                .path("user").path("account").asText()).isEqualTo("second");
    }

    private JsonNode render(JsonView view, Map<String, ?> model) throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        view.render(model, mock(HttpServletRequest.class), response);
        return objectMapper.readTree(body.toString());
    }

    public record SampleResponse(String account) {
    }
}
