package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("모델 데이터가 1개면 값을 그대로 반환")
    @Test
    void renderOneData() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> model = Map.of("key1", "value1");

        // when
        jsonView.render(model, request, response);

        // then
        writer.flush();
        String actualJson = stringWriter.toString().trim();
        String expectedJson = "\"value1\"".trim();

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @DisplayName("모델 데이터가 2개 이상이면 Map 형태로 JSON으로 변환해서 반환")
    @Test
    void renderOverTwoData() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> model = new ConcurrentHashMap<>(Map.of("key1", "value1", "key2", "value2"));

        // when
        jsonView.render(model, request, response);

        // then
        writer.flush();
        String actualJson = stringWriter.toString().trim();
        String expectedJson = """
                {"key1":"value1","key2":"value2"}
                """.trim();

        assertThat(actualJson).isEqualTo(expectedJson);
    }
}
