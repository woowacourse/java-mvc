package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Map;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestObject;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;
    private JsonView jsonView;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        jsonView = new JsonView();
    }

    @DisplayName("model에 데이터가 2개 이상인 경우 JSON 형태로 값을 반환한다.")
    @Test
    void renderWithManyData() throws Exception {
        Map<String, Object> model = Map.of("test", new TestObject());
        jsonView.render(model, request, response);

        String expected = "{\"value\":\"test\",\"value2\":\"test2\"}";
        assertAll(
                () -> verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> verify(writer).write(expected),
                () -> verify(writer).flush()
        );
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void renderWithOneData() throws Exception {
        Map<String, Object> model = Map.of("test", "test");
        jsonView.render(model, request, response);

        String expected = "\"test\"";
        assertAll(
                () -> verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> verify(writer).write(expected),
                () -> verify(writer).flush()
        );
    }
}
