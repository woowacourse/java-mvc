package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter body = new StringWriter();

    @BeforeEach
    void setUp() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void JSON_ContentType으로_응답한다() throws Exception {
        jsonView.render(Map.of("id", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델_데이터가_1개면_값을_그대로_반환한다() throws Exception {
        jsonView.render(Map.of("user", new Sample("gugu", 20)), request, response);

        assertThat(body.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":20}");
    }

    @Test
    void 모델_데이터가_2개_이상이면_Map_형태로_반환한다() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("id", "gugu");
        model.put("age", 20);

        jsonView.render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"id\":\"gugu\",\"age\":20}");
    }

    record Sample(String name, int age) {
    }
}
