package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.valid.Person;

public class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private OutputStream outputStream;
    private PrintWriter writer;
    private JsonView view;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        when(response.getWriter()).thenReturn(writer);
        view = new JsonView();
    }

    @DisplayName("model의 attribute가 1개이면, value를 응답한다.")
    @Test
    void render_unique() throws Exception {
        // given
        Map<String, Object> model = Map.of("key", "value");

        // when
        view.render(model, request, response);
        writer.flush();
        String expected = outputStream.toString();

        // then
        assertThat(expected).isEqualTo("\"value\"");
    }

    @DisplayName("model의 attribute가 2개 이상이면, json 형태로 응답한다.")
    @Test
    void render_json() throws Exception {
        // given
        Map<String, Object> model = Map.of("key1", "value1", "key2", "value2");

        // when
        view.render(model, request, response);
        writer.flush();
        String expected = outputStream.toString();

        // then
        assertThat(expected)
                .contains("\"key1\":\"value1\"")
                .contains("\"key2\":\"value2\"");
    }

    @DisplayName("model의 attribute가 객체이면, json 형태로 응답한다.")
    @Test
    void render_object() throws Exception {
        // given
        Map<String, Object> model = Map.of(
                "reviewer", new Person("atom", 25),
                "reviewee", new Person("myungoh", 25));

        // when
        view.render(model, request, response);
        writer.flush();
        String expected = outputStream.toString();

        // then
        assertThat(expected)
                .contains("\"reviewer\":{\"name\":\"atom\",\"age\":25}")
                .contains("\"reviewee\":{\"name\":\"myungoh\",\"age\":25}");
    }
}
