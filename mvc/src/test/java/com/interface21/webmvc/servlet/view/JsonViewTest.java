package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        this.jsonView = new JsonView();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.stringWriter = new StringWriter();
    }

    @Test
    void renderWithSingleModelAttribute() throws Exception {
        // given
        Map<String, String> model = Map.of("name", "mia");

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        jsonView.render(model, request, response);

        // then
        String jsonResponse = stringWriter.toString();
        assertThat(jsonResponse).isEqualTo("\"mia\"");
    }

    @Test
    void renderWithMultipleModelAttributes() throws Exception {
        // given
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("name", "mia");
        model.put("age", 100);

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        jsonView.render(model, request, response);

        // then
        String jsonResponse = stringWriter.toString();
        assertThat(jsonResponse).isEqualTo("{\"name\":\"mia\",\"age\":100}");
    }
}
