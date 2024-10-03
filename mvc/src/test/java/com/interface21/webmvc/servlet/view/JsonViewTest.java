package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    @DisplayName("model의 크기가 1일 때, response에 그 값만 담도록 한다.")
    void render_oneModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var out = mock(OutputStream.class);

        when(response.getWriter()).thenReturn(new ResponseWriter(out));

        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of("name", "gugu");

        // when
        jsonView.render(model, request, response);

        // then
        ResponseWriter writer = (ResponseWriter) response.getWriter();
        assertThat(writer.value).isEqualTo("gugu");
    }

    @Test
    @DisplayName("model의 크기가 1보다 클 때, response에 키 : 값 형태로 담도록 한다.")
    void render_moreThanOneModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var out = mock(OutputStream.class);

        when(response.getWriter()).thenReturn(new ResponseWriter(out));

        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of("name", "gugu", "email", "gugu@naver.com");

        // when
        jsonView.render(model, request, response);

        // then
        ResponseWriter writer = (ResponseWriter) response.getWriter();
        assertThat(writer.value).isIn(
                "{\"email\":\"gugu@naver.com\",\"name\":\"gugu\"}",
                "{\"name\":\"gugu\",\"email\":\"gugu@naver.com\"}");
    }

    static class ResponseWriter extends PrintWriter {

        String value;

        public ResponseWriter(OutputStream out) {
            super(out);
        }

        @Override
        public void write(String s) {
            value = s;
        }
    }

}
