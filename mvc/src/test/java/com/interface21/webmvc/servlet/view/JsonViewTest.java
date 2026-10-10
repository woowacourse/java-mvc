package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class JsonViewTest {
    @Test
    void 응답의_ContentType은_JSON_UTF8이다() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = Map.of("name", "gugu");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델에_값이_1개면_값만_JSON으로_반환한다() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = Map.of("name", "gugu");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(response.getContentAsString()).isEqualTo("\"gugu\"");
    }

    @Test
    void 모델에_값이_2개_이상이면_Map_형태로_반환한다() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = Map.of("name", "gugu","age",20);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        jsonView.render(model, request, response);

        // then
        String json = response.getContentAsString();
        Map<?, ?> result = new ObjectMapper().readValue(json, Map.class);
        assertThat(result).isEqualTo(model);
    }
}
