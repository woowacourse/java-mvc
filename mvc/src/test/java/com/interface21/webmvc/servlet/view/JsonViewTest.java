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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseBody;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
    }

    @Test
    void rendersEmptyModelAsJsonObject() throws Exception {
        jsonView.render(Map.of(), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(readResponse()).isEqualTo(objectMapper.readTree("{}"));
    }

    @Test
    void rendersSingleModelValueWithoutAttributeName() throws Exception {
        final var model = Map.of("user", Map.of("account", "gugu"));

        jsonView.render(model, request, response);

        assertThat(readResponse()).isEqualTo(objectMapper.readTree("{\"account\":\"gugu\"}"));
    }

    @Test
    void rendersMultipleModelValuesWithAttributeNames() throws Exception {
        final var model = Map.of(
                "user", Map.of("account", "gugu"),
                "message", "success"
        );

        jsonView.render(model, request, response);

        final JsonNode result = readResponse();
        assertThat(result.get("user").get("account").asText()).isEqualTo("gugu");
        assertThat(result.get("message").asText()).isEqualTo("success");
    }

    private JsonNode readResponse() throws Exception {
        return objectMapper.readTree(responseBody.toString());
    }
}
