package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

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
    void ContentType을_JSON으로_설정한다() throws Exception {
        jsonView.render(Map.of("name", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void model에_데이터가_1개면_값을_그대로_반환한다() throws Exception {
        jsonView.render(Map.of("user", new Member("gugu", 20)), request, response);

        assertThat(body.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":20}");
    }

    @Test
    void model에_데이터가_2개_이상이면_Map_형태로_반환한다() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("name", "gugu");
        model.put("age", 20);

        jsonView.render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":20}");
    }

    @Test
    void model이_비어있으면_빈_객체를_반환한다() throws Exception {
        jsonView.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    record Member(String name, int age) {
    }
}
