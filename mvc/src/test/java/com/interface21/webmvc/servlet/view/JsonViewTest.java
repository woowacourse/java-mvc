package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class JsonViewTest {

    @Test
    void renderSingleModelValue() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var model = Map.of("name", "홍길동");

        // when
        new JsonView().render(model, request, response);

        // then
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo("\"홍길동\"");
    }

    @Test
    void renderMultipleModelValues() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var model = Map.of("name", "홍길동", "age", 20);

        // when
        new JsonView().render(model, request, response);
        var mapper = new ObjectMapper();

        // then
        assertThat(mapper.readTree(response.getContentAsString()))
                .isEqualTo(mapper.readTree("{\"name\":\"홍길동\",\"age\":20}"));
    }

    @Test
    void renderEmptyModelValue() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        Map<String, Object> model = Map.of();

        // when
        new JsonView().render(model, request, response);

        // then
        assertThat(response.getContentAsString()).isEqualTo("{}");
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
