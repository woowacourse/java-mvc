package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class JsonViewTest {

    private JsonView jsonView;
    private MockHttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        jsonView = new JsonView();

        request = new MockHttpServletRequest();
        response = mock(HttpServletResponse.class);

        printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @DisplayName("model에 데이터가 없으면 빈 JSON으로 변환해서 반환한다.")
    @Test
    public void renderEmptyModel() throws Exception {
        Map<String, Object> model = new HashMap<>();

        jsonView.render(model, request, response);

        verify(printWriter).write("{}");
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환햔다.")
    @Test
    public void renderSingleModel() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "jerry");

        jsonView.render(model, request, response);

        verify(printWriter).write("\"jerry\"");
    }

    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.")
    @Test
    public void renderMultipleModel() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("reviewee", "jerry");
        model.put("reviewer", "coli");

        jsonView.render(model, request, response);

        verify(printWriter).write("{\"reviewee\":\"jerry\",\"reviewer\":\"coli\"}");
    }
}
