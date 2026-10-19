package com.interface21.webmvc.servlet.view;

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

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter body = new StringWriter();

    @BeforeEach
    void setUp() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void rendersSingleModelValueAsJson() throws Exception {
        view.render(Map.of("user", new TestUser("구구")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"구구\"}"));
    }

    @Test
    void rendersMultipleModelValuesAsMap() throws Exception {
        view.render(Map.of("user", new TestUser("gugu"), "count", 1),
                request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":1}"));
    }

    @Test
    void rendersEmptyModelAsEmptyObject() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    public record TestUser(String account) {
    }
}
