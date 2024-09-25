package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        jsonView = new JsonView();
        request = Mockito.mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @DisplayName("모델에 데이터가 한 개인 경우 그대로 반환한다.")
    @Test
    void renderSingleModel() throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put("key", "value");

        jsonView.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo("\"value\"");
    }

    @DisplayName("모델에 데이터가 두 개 이상이면 Map 형태 그대로 반환한다.")
    @Test
    void renderMultipleModels() throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put("key1", "value1");
        model.put("key2", "value2");

        jsonView.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

    @DisplayName("모델에 데이터가 없는 경우 비어 있는 Map을 반환한다.")
    @Test
    void renderEmptyModel() throws Exception {
        Map<String, Object> model = new HashMap<>();

        jsonView.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo("{}");
    }
}
