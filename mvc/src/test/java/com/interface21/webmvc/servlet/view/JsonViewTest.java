package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("JSON의 ContentType은 application/json utf8 형태이다.")
    void contentType() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter())
                .thenReturn(printWriter);
        JsonView jsonView = new JsonView();
        Map<String, String> model = Map.of("name", "atom");

        jsonView.render(model, request, response);

        verify(response, atLeastOnce())
                .setContentType("application/json;charset=UTF-8");
    }

    @Test
    @DisplayName("model 데이터가 1개면 값을 그대로 반환한다.")
    void renderSingleModel() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter())
                .thenReturn(printWriter);
        JsonView jsonView = new JsonView();
        Map<String, String> model = Map.of("name", "atom");

        jsonView.render(model, request, response);

        verify(printWriter, atLeastOnce())
                .write("\"atom\"");
    }

    @Test
    @DisplayName("model 데이터가 2개 이상이면 map 형태 그대로 반환한다.")
    void renderMultipleModel() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter())
                .thenReturn(printWriter);
        JsonView jsonView = new JsonView();

        Map<String, TestData> model = new HashMap<>();
        model.put("a", new TestData("a", 1));
        model.put("b", new TestData("b", 2));

        jsonView.render(model, request, response);

        verify(printWriter, atLeastOnce())
                .write("{\"a\":{\"name\":\"a\",\"age\":1},\"b\":{\"name\":\"b\",\"age\":2}}");
    }

    private record TestData(String name, int age) {
    }
}
