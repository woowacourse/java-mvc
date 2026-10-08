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
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void 모델이_하나면_값만_JSON으로_응답한다() throws Exception {
        StringWriter output = responseWriter();

        jsonView.render(Map.of("user", new User("quda")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(output.toString()).isEqualTo("{\"account\":\"quda\"}");
    }

    @Test
    void 모델이_여러_개면_Map을_JSON으로_응답한다() throws Exception {
        StringWriter output = responseWriter();
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("name", "quda");
        model.put("age", 20);

        jsonView.render(model, request, response);

        assertThat(output.toString()).isEqualTo("{\"name\":\"quda\",\"age\":20}");
    }

    private StringWriter responseWriter() throws Exception {
        StringWriter output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        return output;
    }

    record User(String account) {
    }
}
