package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body, true));
    }

    @Test
    void Content_Type을_JSON_UTF8로_설정한다() throws Exception {
        final JsonView jsonView = new JsonView();

        jsonView.render(Map.of("user", new SampleUser("gugu", "gugu@woowahan.com")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델이_하나면_값을_그대로_JSON으로_응답한다() throws Exception {
        final JsonView jsonView = new JsonView();

        jsonView.render(Map.of("user", new SampleUser("gugu", "gugu@woowahan.com")), request, response);

        final JsonNode expected = objectMapper.readTree("{\"account\":\"gugu\",\"email\":\"gugu@woowahan.com\"}");
        assertThat(objectMapper.readTree(body.toString())).isEqualTo(expected);
    }

    @Test
    void 모델이_하나이고_문자열이면_JSON_문자열로_응답한다() throws Exception {
        final JsonView jsonView = new JsonView();

        jsonView.render(Map.of("account", "gugu"), request, response);

        assertThat(objectMapper.readTree(body.toString())).isEqualTo(objectMapper.readTree("\"gugu\""));
    }

    @Test
    void 모델이_두_개_이상이면_Map_형태_그대로_JSON으로_응답한다() throws Exception {
        final JsonView jsonView = new JsonView();
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", new SampleUser("gugu", "gugu@woowahan.com"));
        model.put("count", 2);

        jsonView.render(model, request, response);

        final JsonNode expected = objectMapper.readTree(
                "{\"user\":{\"account\":\"gugu\",\"email\":\"gugu@woowahan.com\"},\"count\":2}");
        assertThat(objectMapper.readTree(body.toString())).isEqualTo(expected);
    }

    record SampleUser(String account, String email) {
    }
}
