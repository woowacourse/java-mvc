package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    void rendersModelAsUtf8Json() throws Exception {
        Map<String, ?> model = Map.of(
                "name", "녀녕",
                "message", "줄바꿈\n\"따옴표\"",
                "items", Arrays.asList(1, true, null),
                "user", Map.of("id", 1));
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(model, mock(HttpServletRequest.class), response);

        InOrder order = inOrder(response);
        order.verify(response).setContentType("application/json;charset=UTF-8");
        order.verify(response).getWriter();
        ObjectMapper mapper = new ObjectMapper();
        assertThat(mapper.readTree(body.toString())).isEqualTo(mapper.valueToTree(model));
    }

    @Test
    void rendersSingleValueWithoutModelKey() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("user", Map.of("account", "gugu")),
                mock(HttpServletRequest.class), response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void rendersSingleNullValueAsJsonNull() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Collections.singletonMap("user", null),
                mock(HttpServletRequest.class), response);

        assertThat(body.toString()).isEqualTo("null");
    }

    @Test
    void rendersEmptyModelAsJsonObject() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of(), mock(HttpServletRequest.class), response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    @Test
    void propagatesSerializationFailureWithoutWritingResponse() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        assertThatThrownBy(() -> new JsonView().render(
                Map.of("unsupported", new Object()), mock(HttpServletRequest.class), response))
                .isInstanceOf(JsonProcessingException.class);
        verifyNoInteractions(response);
    }
}
