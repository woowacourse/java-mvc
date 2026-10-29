package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonView jsonView = new JsonView();
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseBody;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
    }

    @Test
    void rendersSingleModelValueWithoutAttributeName() throws Exception {
        jsonView.render(Map.of("user", Map.of("account", "구구")), request, response);

        final var responseOrder = inOrder(response);
        responseOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        responseOrder.verify(response).setCharacterEncoding("UTF-8");
        responseOrder.verify(response).getWriter();
        assertThat(objectMapper.readTree(responseBody.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"구구\"}"));
    }

    @Test
    void rendersMultipleModelAttributesAsJsonObject() throws Exception {
        jsonView.render(Map.of("user", Map.of("account", "gugu"), "count", 1), request, response);

        assertThat(objectMapper.readTree(responseBody.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":1}"));
    }

    @Test
    void rendersEmptyModelAsEmptyJsonObject() throws Exception {
        jsonView.render(Map.of(), request, response);

        assertThat(responseBody.toString()).isEqualTo("{}");
    }

    @Test
    void rendersSingleNullModelValueAsJsonNull() throws Exception {
        jsonView.render(Collections.singletonMap("user", null), request, response);

        assertThat(responseBody.toString()).isEqualTo("null");
    }
}
