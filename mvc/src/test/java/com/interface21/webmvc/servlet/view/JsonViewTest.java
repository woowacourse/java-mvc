package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    void setsJsonContentType() throws Exception {
        jsonView.render(Map.of("id", 1), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void rendersValueDirectlyWhenModelHasSingleEntry() throws Exception {
        jsonView.render(Map.of("user", new Sample("gugu", 1)), request, response);

        assertThat(writer.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":1}");
    }

    @Test
    void rendersModelAsMapWhenModelHasMultipleEntries() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("id", 1);
        model.put("name", "gugu");

        jsonView.render(model, request, response);

        assertThat(writer.toString()).isEqualTo("{\"id\":1,\"name\":\"gugu\"}");
    }

    @Test
    void rendersEmptyObjectWhenModelIsEmpty() throws Exception {
        jsonView.render(Map.of(), request, response);

        assertThat(writer.toString()).isEqualTo("{}");
    }

    record Sample(String name, int age) {
    }
}
