package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter body = new StringWriter();

    @Test
    void render_ThenSetJsonContentTypeBeforeGettingWriter() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("id", "gugu"), request, response);

        final InOrder inOrder = inOrder(response);
        inOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        inOrder.verify(response).getWriter();
    }

    @Test
    void render_WhenModelHasMultipleEntries_ThenWriteJsonObject() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("id", "gugu", "age", 20), request, response);
        assertThat(readTree(body.toString()))
                .isEqualTo(readTree("""
                {"id": "gugu", "age": 20}
                """));
    }

    @Test
    void render_WhenModelHasSingleEntry_ThenWriteNoKey() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("id", "gugu"), request, response);
        assertThat(readTree(body.toString()))
                .isEqualTo(readTree("""
                "gugu"
                """));
    }

    private JsonNode readTree(String string) throws Exception {
        return objectMapper.readTree(string);
    }
}
