package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private PrintWriter mockWriter;
    private ArgumentCaptor<String> responseCaptor;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockWriter = mock(PrintWriter.class);
        responseCaptor = ArgumentCaptor.forClass(String.class);

        when(mockResponse.getWriter()).thenReturn(mockWriter);
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void renderSingleValue() throws Exception {
        Map<String, Object> model = Map.of("key1", "value1");

        jsonView.render(model, mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json;charset=UTF-8");
        verify(mockWriter).write(responseCaptor.capture());

        String expected = "\"value1\"";
        assertThat(responseCaptor.getValue()).isEqualTo(expected);
    }

    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해 반환한다.")
    @Test
    void renderMultipleValues() throws Exception {
        Map<String, Object> model = Map.of("key1", "value1", "key2", "value2");

        jsonView.render(model, mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json;charset=UTF-8");
        verify(mockWriter).write(responseCaptor.capture());

        String expected = "{\"key2\":\"value2\",\"key1\":\"value1\"}";
        assertThat(responseCaptor.getValue()).isEqualTo(expected);
    }
}
