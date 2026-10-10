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
    void ContentType을_JSON으로_설정한다() throws Exception {
        new JsonView().render(Map.of("account", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void model에_데이터가_1개면_값을_그대로_반환한다() throws Exception {
        new JsonView().render(Map.of("user", new TestUser("gugu", 20)), request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\",\"age\":20}");
    }

    @Test
    void model에_데이터가_2개_이상이면_Map_형태_그대로_반환한다() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", new TestUser("gugu", 20));
        model.put("message", "안녕");

        new JsonView().render(model, request, response);

        assertThat(body.toString())
                .isEqualTo("{\"user\":{\"account\":\"gugu\",\"age\":20},\"message\":\"안녕\"}");
    }

    @Test
    void model이_비어있으면_빈_JSON_객체를_반환한다() throws Exception {
        new JsonView().render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    private record TestUser(String account, int age) {
    }
}
