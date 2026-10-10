package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("모델 데이터가 하나면 속성 이름 없이 값만 JSON으로 쓴다")
    void rendersSingleModelValueWithoutAttributeName() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("user", Map.of("account", "gugu")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        JsonNode json = objectMapper.readTree(body.toString());
        assertThat(json.get("account").asText()).isEqualTo("gugu");
        assertThat(json.has("user")).isFalse();
    }

    @Test
    @DisplayName("모델 데이터가 여러 개면 모델 전체를 JSON 객체로 쓴다")
    void rendersMultipleModelValuesAsJsonObject() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("account", "gugu", "active", true), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        JsonNode json = objectMapper.readTree(body.toString());
        assertThat(json.get("account").asText()).isEqualTo("gugu");
        assertThat(json.get("active").asBoolean()).isTrue();
    }
}
