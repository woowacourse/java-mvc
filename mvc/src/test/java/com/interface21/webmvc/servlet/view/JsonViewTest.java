package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    @DisplayName("모델 항목이 하나면 키 없이 값 자체를 JSON으로 응답한다")
    void rendersSingleValue() throws Exception {
        view.render(Map.of("user", new UserData("마이찬")), request, response);

        assertJson("""
                {"account": "마이찬"}
                """);
    }

    @Test
    @DisplayName("모델 항목이 여러 개면 Map 전체를 JSON으로 응답한다")
    void rendersEntireModel() throws Exception {
        view.render(Map.of("user", new UserData("마이찬"), "count", 1), request, response);

        assertJson("""
                {"user": {"account": "마이찬"}, "count": 1}
                """);
    }

    @Test
    @DisplayName("빈 모델은 빈 JSON 객체로 응답한다")
    void rendersEmptyModel() throws Exception {
        view.render(Map.of(), request, response);

        assertJson("{}");
    }

    @Test
    @DisplayName("모델의 유일한 값이 null이면 JSON null로 응답한다")
    void rendersSingleNullValue() throws Exception {
        view.render(Collections.singletonMap("user", null), request, response);

        assertThat(response.getContentAsString()).isEqualTo("null");
    }

    @Test
    @DisplayName("JSON Content-Type과 UTF-8 인코딩으로 한글을 응답한다")
    void rendersUtf8Response() throws Exception {
        view.render(Map.of("message", "안녕하세요"), request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(response.getContentAsByteArray())
                .isEqualTo("\"안녕하세요\"".getBytes(StandardCharsets.UTF_8));
    }

    private void assertJson(final String expected) throws Exception {
        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree(expected));
    }

    public record UserData(String account) {
    }
}
