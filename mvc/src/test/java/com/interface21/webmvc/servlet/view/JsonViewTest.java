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
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView view;
    private ObjectMapper objectMapper;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        view = new JsonView();
        objectMapper = new ObjectMapper();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    void singleMap() throws Exception {
        final var model = Map.of("user", Map.of("account", "초록"));

        view.render(model, request, response);

        final var inOrder = inOrder(response);
        inOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        inOrder.verify(response).getWriter();
        assertThat(objectMapper.readTree(writer.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"초록\"}"));
    }

    @Test
    void singleJavaObject() throws Exception {
        final var model = Map.of("user", new TestUser("초록"));

        view.render(model, request, response);

        assertThat(objectMapper.readTree(writer.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"초록\"}"));
    }

    @Test
    void multipleObjects() throws Exception {
        final var model = Map.of("user", Map.of("account", "gugu"), "count", 1);

        view.render(model, request, response);

        assertThat(objectMapper.readTree(writer.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":1}"));
    }

    @Test
    void singleCollection() throws Exception {
        final var model = Map.of("users", List.of(Map.of("account", "gugu"), Map.of("account", "초록")));

        view.render(model, request, response);

        assertThat(objectMapper.readTree(writer.toString()))
                .isEqualTo(objectMapper.readTree("[{\"account\":\"gugu\"},{\"account\":\"초록\"}]"));
    }

    @Test
    void singleString() throws Exception {
        final String message = "초록의 \"하루\"\n기록";

        view.render(Map.of("message", message), request, response);

        assertThat(objectMapper.readTree(writer.toString()).asText()).isEqualTo(message);
    }

    @Test
    void emptyModel() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(writer.toString()).isEqualTo("{}");
    }

    @Test
    void singleNullValue() throws Exception {
        view.render(Collections.singletonMap("user", null), request, response);

        assertThat(writer.toString()).isEqualTo("null");
    }

    public static class TestUser {

        private final String account;

        public TestUser(final String account) {
            this.account = account;
        }

        public String getAccount() {
            return account;
        }
    }
}
