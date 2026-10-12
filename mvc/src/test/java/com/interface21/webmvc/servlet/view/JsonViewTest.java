package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonView view = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter body = new StringWriter();

    @BeforeEach
    void prepareResponseWriter() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void rendersSingleObjectWithoutItsModelKeyAndSetsContentTypeBeforeWriting() throws Exception {
        final var model = Map.of("user", new Profile("moa", "모아"));

        view.render(model, request, response);

        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        assertThat(objectMapper.readTree(body.toString())).isEqualTo(objectMapper.readTree(
                "{\"account\":\"moa\",\"name\":\"모아\"}"));
    }

    @Test
    void rendersSingleStringAsAJsonString() throws Exception {
        view.render(Map.of("message", "안녕"), request, response);

        assertThat(body.toString()).isEqualTo("\"안녕\"");
    }

    @Test
    void rendersMultipleModelEntriesWithTheirKeys() throws Exception {
        view.render(Map.of("message", "안녕", "count", 2), request, response);

        assertThat(objectMapper.readTree(body.toString())).isEqualTo(objectMapper.readTree(
                "{\"message\":\"안녕\",\"count\":2}"));
    }

    @Test
    void rendersEmptyModelAsAnEmptyJsonObject() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    @Test
    void rendersSingleNullValueAsJsonNull() throws Exception {
        view.render(Collections.singletonMap("user", null), request, response);

        assertThat(body.toString()).isEqualTo("null");
    }

    public static class Profile {

        private final String account;
        private final String name;

        public Profile(final String account, final String name) {
            this.account = account;
            this.name = name;
        }

        public String getAccount() {
            return account;
        }

        public String getName() {
            return name;
        }
    }
}
