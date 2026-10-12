package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void rendersSingleValueWithoutModelKey() throws Exception {
        final var response = render(Map.of("user", Map.of("account", "기영")));

        
        assertThat(mapper.readTree(response.toString()))
                .isEqualTo(mapper.readTree("{\"account\":\"기영\"}"));
    }

    @Test
    void rendersMultipleValuesWithModelKeys() throws Exception {
        final var response = render(Map.of("user", Map.of("account", "giyoul"), "count", 1));

        assertThat(mapper.readTree(response.toString()))
                .isEqualTo(mapper.readTree("{\"user\":{\"account\":\"giyoul\"},\"count\":1}"));
    }

    @Test
    void rendersEmptyModelAsEmptyObject() throws Exception {
        assertThat(render(Map.of()).toString()).isEqualTo("{}");
    }

    @Test
    void rendersSingleNullValue() throws Exception {
        assertThat(render(Collections.singletonMap("user", null)).toString()).isEqualTo("null");
    }

    private StringWriter render(final Map<String, ?> model) throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        new JsonView().render(model, mock(HttpServletRequest.class), response);
        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        return output;
    }
}
