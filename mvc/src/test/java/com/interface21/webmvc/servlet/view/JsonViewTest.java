package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @DisplayName("JSON 응답 시 Content-Type은 application/json;charset=UTF-8 이다.")
    @Test
    void contentTypeTest() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        // when
        jsonView.render(new HashMap<>(), request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @DisplayName("응답할 데이터가 하나 뿐이라면 값을 그대로 반환한다.")
    @Test
    void renderSingleDataTest() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        String key = "key";
        String value = "value";
        Map<String, Object> model = Map.of(key, value);

        // when
        jsonView.render(model, request, response);

        // then
        verify(printWriter).write(new ObjectMapper().writeValueAsString(value));
    }

    @DisplayName("응답할 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환하여 응답한다.")
    @Test
    void renderMultipleDataTest() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = Map.of("key1", "value1", "key2", "value2");

        // when
        jsonView.render(model, request, response);

        // then
        verify(printWriter).write(new ObjectMapper().writeValueAsString(model));
    }

    @DisplayName("직렬화 불가능한 객체를 전달하면 예외가 발생한다.")
    @Test
    void throwsExceptionWhenTransientObject() {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("key", new CannotBeSerialized());

        // when & then
         assertThatThrownBy(() -> jsonView.render(model, request, response))
                 .isInstanceOf(JsonProcessingException.class);
    }

    @SuppressWarnings("unused")
    private static class CannotBeSerialized {
        private final CannotBeSerialized self = this;
    }
}
