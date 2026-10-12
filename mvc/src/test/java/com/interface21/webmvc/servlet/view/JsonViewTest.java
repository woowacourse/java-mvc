package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.InOrder;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void singleValueIsRenderedWithoutItsModelKey() throws Exception {
        final String body = render(Map.of("user", Map.of("account", "카키")));

        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.readTree("{\"account\":\"카키\"}"));
    }

    @Test
    void multipleValuesRetainTheirModelKeys() throws Exception {
        final Map<String, Object> model = Map.of("user", Map.of("account", "kaki"), "count", 1);
        final String body = render(model);

        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.valueToTree(model));
    }

    @Test
    void emptyModelIsAnEmptyObject() throws Exception {
        assertThat(render(Map.of())).isEqualTo("{}");
    }

    @Test
    void singleNullValueIsJsonNull() throws Exception {
        assertThat(render(Collections.singletonMap("user", null))).isEqualTo("null");
    }

    private String render(final Map<String, ?> model) throws Exception {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        new JsonView().render(model, mock(HttpServletRequest.class), response);
        final InOrder order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        return body.toString();
    }
}
