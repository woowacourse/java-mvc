package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JsonViewTest {

    @DisplayName("model이 존재하지 않으면 예외를 발생한다.")
    @Test
    void render_whenModelIsEmpty() {
        // given
        Map<String, ?> model = new HashMap<>();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JsonView jsonView = new JsonView();
        // when && then
        assertThatThrownBy(() -> jsonView.render(model, request, response))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("렌더링할 모델이 존재하지 않습니다. ");

    }

    @DisplayName("하나의 값만 있는 model은 값을 그대로 반환한다.")
    @Test
    void render_whenModelIsSingle() throws Exception {
        // given
        Map<String, String> model = new HashMap<>();
        model.put("key", "value");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JsonView jsonView = new JsonView();
        // when
        jsonView.render(model, request, response);
        response.getContentAsString();

        // then
        Assertions.assertAll(
                () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> assertThat(response.getContentAsString()).isEqualTo("value")
        );
    }

    @DisplayName("model을 Json 형태로 반환한다.")
    @Test
    void render() throws Exception {
        // given
        Map<String, String> model = new HashMap<>();
        model.put("key", "value");
        model.put("key2", "value2");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        JsonView jsonView = new JsonView();
        // when
        jsonView.render(model, request, response);
        response.getContentAsString();

        // then
        Assertions.assertAll(
                () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> assertThat(response.getContentAsString()).isEqualTo("{\"key2\":\"value2\",\"key\":\"value\"}")
        );
    }
}
