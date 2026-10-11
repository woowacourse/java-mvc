package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import testsupport.TestResponse;
import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 모델_항목이_하나면_값만_반환한다() throws Exception {
        TestResponse response = new TestResponse();

        view.render(Map.of("user", Map.of("account", "gugu")),
                mock(HttpServletRequest.class), response.response);

        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree("""
                        {"account":"gugu"}
                        """));
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델_항목이_여러개면_Map_전체를_반환한다() throws Exception {
        TestResponse response = new TestResponse();

        view.render(Map.of("user", Map.of("account", "gugu"), "count", 1),
                mock(HttpServletRequest.class), response.response);

        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree("""
                        {"user":{"account":"gugu"},"count":1}
                        """));
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 한글을_UTF8로_응답한다() throws Exception {
        TestResponse response = new TestResponse();

        view.render(Map.of("message", "안녕하세요"), mock(HttpServletRequest.class), response.response);

        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(objectMapper.readTree(response.getContentAsByteArray()).asText())
                .isEqualTo("안녕하세요");
    }
}
