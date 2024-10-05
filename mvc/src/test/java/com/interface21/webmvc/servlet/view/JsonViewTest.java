package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.http.MediaType;

class JsonViewTest {

    ByteArrayOutputStream outputStream;
    PrintStream out;

    @BeforeEach
    void setUp() {
        out = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("JSON으로 응답할 때 ContentType을 MediaType.APPLICATION_JSON_UTF8_VALUE로 반환한다.")
    void return_json_utf8() throws IOException {
        //given
        final Map<String, String> model = Map.of("account", "redddy");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final PrintWriter print = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(print);

        //when
        final JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        //then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 작성한다.")
    void one_data() throws IOException {
        //given
        final Map<String, String> model = Map.of("account", "redddy");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final JsonView jsonView = new JsonView();

        when(response.getWriter()).thenReturn(new PrintWriter(new OutputStreamWriter(System.out)));
        System.setOut(out);

        //when
        jsonView.render(model, request, response);

        //then
        response.getWriter().flush();
        assertThat(outputStream.toString()).isEqualTo("\"redddy\"");
    }

    @Test
    @DisplayName("model에 데이터가 2개면 Map 형태로 작성한다.")
    void two_data() throws IOException {
        //given
        final Map<String, String> model = new LinkedHashMap<>();
        model.put("account", "redddy");
        model.put("password", "486");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final JsonView jsonView = new JsonView();

        when(response.getWriter()).thenReturn(new PrintWriter(new OutputStreamWriter(System.out)));

        System.setOut(out);

        //when
        jsonView.render(model, request, response);

        //then
        response.getWriter().flush();
        assertThat(outputStream.toString()).isEqualTo("{\"account\":\"redddy\",\"password\":\"486\"}");
    }
}
