package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter responseBody = new StringWriter();

    @BeforeEach
    void setUp() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
    }

    @Test
    void render_SingleModelEntry_WritesValueAsUtf8Json() throws Exception {
        Map<String, ?> model = Map.of("user", Map.of("account", "이든"));

        jsonView.render(model, request, response);

        assertThat(responseBody.toString()).isEqualTo("{\"account\":\"이든\"}");
        InOrder responseOrder = inOrder(response);
        responseOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        responseOrder.verify(response).getWriter();
    }

    @Test
    void render_MultipleModelEntries_WritesMapAsJson() throws Exception {
        Map<String, ?> model = Map.of("user", Map.of("account", "gugu"), "count", 1);

        jsonView.render(model, request, response);

        ObjectMapper objectMapper = new ObjectMapper();
        assertThat(objectMapper.readTree(responseBody.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":1}"));
    }
}
