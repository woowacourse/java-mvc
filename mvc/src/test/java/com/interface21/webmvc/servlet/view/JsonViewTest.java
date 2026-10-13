package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    @DisplayName("빈 모델은 빈 JSON 객체로 응답한다")
    void rendersEmptyModelAsJsonObject() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree("{}"));
    }

    @Test
    @DisplayName("모델 항목이 하나면 최상위 키 없이 값만 응답한다")
    void rendersSingleModelValueWithoutKeyWrapper() throws Exception {
        view.render(Map.of("user", new TestUser("gugu")), request, response);

        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"gugu\"}"));
    }

    @Test
    @DisplayName("모델 항목이 여러 개면 각 키와 값을 유지한다")
    void rendersMultipleModelValuesWithKeys() throws Exception {
        final Map<String, Object> model = Map.of("user", new TestUser("gugu"), "count", 1);

        view.render(model, request, response);

        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree("""
                        {"user": {"account": "gugu"}, "count": 1}
                        """));
    }

    @Test
    @DisplayName("JSON Content-Type과 UTF-8 인코딩을 설정한다")
    void setsJsonContentTypeAndUtf8Encoding() throws Exception {
        view.render(Map.of("user", new TestUser("gugu")), request, response);

        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
    }

    @Test
    @DisplayName("한글 응답 바이트를 UTF-8로 읽으면 원문이 유지된다")
    void preservesKoreanTextInUtf8ResponseBytes() throws Exception {
        view.render(Map.of("message", "안녕하세요"), request, response);

        final String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.readTree("\"안녕하세요\""));
    }

    public static class TestUser {

        private final String account;

        public TestUser(final String account) {
            this.account = account;
        }

        public String getAccount() {
            return account;
        }
    }
}
