package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private JsonView jsonView;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        jsonView = new JsonView();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @DisplayName("Json 렌더링 성공 : model에 값이 1개만 존재할 때")
    @Test
    void render1() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        Map<String, ?> model = Map.of("test-key", "test-value");
        jsonView.render(model, request, response);
        printWriter.flush();

        // then
        String jsonResponse = stringWriter.toString();
        Assertions.assertThat(jsonResponse).isEqualTo("\"test-value\"");
    }

    @DisplayName("Json 렌더링 성공 : model에 값이 2개이상 존재할 때")
    @Test
    void renderOver2() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        Map<String, ?> model = Map.of("test-key1", "test-value",
                "test-key2", "test-value");
        jsonView.render(model, request, response);
        printWriter.flush();

        // then
        String jsonResponse = stringWriter.toString();
        Assertions.assertThat(jsonResponse).contains("test-key1", "test-key2");
    }
}
