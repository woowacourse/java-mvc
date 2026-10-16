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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter output;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
    }

    @Test
    void renderSingleModelValueWithoutAttributeName() throws Exception {
        new JsonView().render(Map.of("user", Map.of("account", "gugu")), request, response);

        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"gugu\"}"));

        final InOrder inOrder = inOrder(response);
        inOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        inOrder.verify(response).getWriter();
    }

    @Test
    void renderMultipleModelEntriesAsObject() throws Exception {
        new JsonView().render(Map.of("name", "gugu", "age", 20), request, response);

        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("{\"name\":\"gugu\",\"age\":20}"));
    }

    @Test
    void renderEmptyModelAsEmptyObject() throws Exception {
        new JsonView().render(Map.of(), request, response);

        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("{}"));
    }
}
