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
    void 모델이_하나면_값만_JSON으로_쓴다() throws Exception {
        final Map<String, Object> model = Map.of("user", Map.of("account", "gugu"));

        new JsonView().render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void 모델이_둘_이상이면_맵을_통째로_JSON으로_쓴다() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", Map.of("account", "gugu"));
        model.put("count", 3);

        new JsonView().render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"user\":{\"account\":\"gugu\"},\"count\":3}");
    }

    @Test
    void Content_Type을_JSON으로_설정한다() throws Exception {
        new JsonView().render(Map.of("user", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
