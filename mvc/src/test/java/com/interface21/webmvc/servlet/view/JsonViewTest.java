package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonView view = new JsonView();
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
    void rendersSingleObjectWithoutModelKey() throws Exception {
        final var user = new UserInfo("이안", "eian@example.com");

        view.render(Map.of("user", user), request, response);

        assertThat(objectMapper.readTree(body.toString())).isEqualTo(objectMapper.valueToTree(user));
        assertThat(body.toString()).contains("이안");
        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
    }

    @Test
    void rendersMultipleValuesWithModelKeys() throws Exception {
        final var model = Map.of("user", new UserInfo("eian", "eian@example.com"), "count", 1);

        view.render(model, request, response);

        assertThat(objectMapper.readTree(body.toString())).isEqualTo(objectMapper.valueToTree(model));
    }

    @Test
    void rendersSingleCollectionAsArray() throws Exception {
        final var users = List.of(new UserInfo("eian", "eian@example.com"));

        view.render(Map.of("users", users), request, response);

        assertThat(objectMapper.readTree(body.toString())).isEqualTo(objectMapper.valueToTree(users));
    }

    @Test
    void rendersEmptyModelAsEmptyObject() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    @Test
    void rendersSingleNullValue() throws Exception {
        final Map<String, Object> model = new HashMap<>();
        model.put("user", null);

        view.render(model, request, response);

        assertThat(body.toString()).isEqualTo("null");
    }

    public record UserInfo(String account, String email) {
    }
}
