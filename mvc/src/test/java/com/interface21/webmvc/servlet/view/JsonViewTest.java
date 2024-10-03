package com.interface21.webmvc.servlet.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @DisplayName("model이 1개의 객체만 포함할 때 단일 객체를 JSON으로 렌더링한다.")
    @Test
    void renderSingleObject() throws Exception {
        // given
        ModelAndView modelAndView = new ModelAndView(jsonView);
        modelAndView.addObject("name", "gugu");

        // when
        jsonView.render(modelAndView.getModel(), request, response);

        // then
        assertThat(responseWriter.toString()).isEqualTo("gugu");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @DisplayName("model이 여러 객체를 포함할 때 Map을 JSON으로 렌더링한다.")
    @Test
    void renderMultipleObjects() throws Exception {
        // given
        ModelAndView modelAndView = new ModelAndView(jsonView);
        modelAndView.addObject("name", "gugu")
                .addObject("age", 20);

        // when
        jsonView.render(modelAndView.getModel(), request, response);

        // then
        assertThat(responseWriter.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":20}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @DisplayName("빈 모델이 주어졌을 때 빈 JSON 객체를 렌더링한다.")
    @Test
    void renderEmptyModel() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(responseWriter.toString()).isEqualTo("{}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
