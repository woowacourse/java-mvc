package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void writesJsonContentType() throws Exception {
        new JsonView().render(Map.of("id", 1), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void writesSingleModelValueAsIs() throws Exception {
        new JsonView().render(Map.of("user", new Sample(1, "gugu")), request, response);

        final JsonNode json = objectMapper.readTree(body.toString());
        assertThat(json.get("id").asInt()).isEqualTo(1);
        assertThat(json.get("account").asText()).isEqualTo("gugu");
        assertThat(json.has("user")).isFalse();
    }

    @Test
    void writesSingleScalarValueAsIs() throws Exception {
        new JsonView().render(Map.of("count", 3), request, response);

        assertThat(body.toString()).isEqualTo("3");
    }

    @Test
    void writesSingleCollectionValueAsIs() throws Exception {
        new JsonView().render(Map.of("names", List.of("a", "b")), request, response);

        assertThat(body.toString()).isEqualTo("[\"a\",\"b\"]");
    }

    @Test
    void writesWholeModelAsObjectWhenMultipleValues() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", new Sample(1, "gugu"));
        model.put("count", 2);

        new JsonView().render(model, request, response);

        final JsonNode json = objectMapper.readTree(body.toString());
        assertThat(json.get("user").get("account").asText()).isEqualTo("gugu");
        assertThat(json.get("count").asInt()).isEqualTo(2);
    }

    @Test
    void writesEmptyObjectWhenModelIsEmpty() throws Exception {
        new JsonView().render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    record Sample(long id, String account) {
    }
}
