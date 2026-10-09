package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("모델 항목이 하나면 최상위 키를 제외하고 값만 응답한다")
    void rendersSingleValue() throws Exception {
        JsonNode json = render(Map.of("user", Map.of("account", "구구")));

        assertThat(json).isEqualTo(objectMapper.readTree("{\"account\":\"구구\"}"));
    }

    @Test
    @DisplayName("모델 항목이 여러 개면 각 키를 유지해 전체 모델을 응답한다")
    void rendersEntireModel() throws Exception {
        JsonNode json = render(Map.of("user", Map.of("account", "gugu"), "count", 1));

        assertThat(json).isEqualTo(objectMapper.readTree("""
                {"user": {"account": "gugu"}, "count": 1}
                """));
    }

    @Test
    @DisplayName("빈 모델은 빈 JSON 객체로 응답한다")
    void rendersEmptyModel() throws Exception {
        assertThat(render(Map.of())).isEqualTo(objectMapper.readTree("{}"));
    }

    private JsonNode render(Map<String, ?> model) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        new JsonView().render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return objectMapper.readTree(output.toString());
    }
}
