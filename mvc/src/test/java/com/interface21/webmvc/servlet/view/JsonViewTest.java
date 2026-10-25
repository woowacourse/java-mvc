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
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void renderValueWhenModelHasOneEntry() throws Exception {
        jsonView.render(Map.of("user", new Sample("gugu", 20)), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":20}");
    }

    @Test
    void renderMapWhenModelHasManyEntries() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("account", "gugu");
        model.put("age", 20);

        jsonView.render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\",\"age\":20}");
    }

    @Test
    void renderEmptyObjectWhenModelIsEmpty() throws Exception {
        jsonView.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    record Sample(String name, int age) {
    }
}
