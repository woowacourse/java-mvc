package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import nextstep.fixtures.HttpServletFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    public static class MyWriter extends PrintWriter {

        private StringBuilder cache;

        public MyWriter(OutputStream out) {
            super(out);
            this.cache = new StringBuilder();
        }

        @Override
        public void write(String s) {
            cache.append(s);
            super.write(s);
        }

        public String getCache() {
            return cache.toString();
        }
    }


    @Test
    @DisplayName("json view를 렌더링한다")
    void render() throws IOException {
        // given
        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("hello", "world");

        HttpServletResponse response = HttpServletFixtures.createResponse();
        MyWriter writer = new MyWriter(new BufferedOutputStream(new ByteArrayOutputStream()));
        when(response.getWriter()).thenReturn(writer);

        // when & then
        assertThatNoException().isThrownBy(
                () -> view.render(model, null, response)
        );
    }

    @Test
    @DisplayName("model 값이 하나라면 해당 값의 내부를 직렬화한다")
    void renderInside() throws IOException {
        // given
        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("hello", "world");

        HttpServletResponse response = HttpServletFixtures.createResponse();
        MyWriter writer = new MyWriter(new BufferedOutputStream(new ByteArrayOutputStream()));
        when(response.getWriter()).thenReturn(writer);
        // when
        view.render(model, null, response);

        // then
        assertThat(writer.getCache()).isEqualTo("\"world\"");
    }

    @Test
    @DisplayName("model 값이 여러개라면 모델 자체를 직렬화한다")
    void renderAll() throws IOException {
        // given
        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("hello", "world");
        model.put("name", "chalee");

        HttpServletResponse response = HttpServletFixtures.createResponse();
        MyWriter writer = new MyWriter(new BufferedOutputStream(new ByteArrayOutputStream()));
        when(response.getWriter()).thenReturn(writer);
        // when
        view.render(model, null, response);

        // then
        assertThat(writer.getCache()).isEqualTo("{\"name\":\"chalee\",\"hello\":\"world\"}");
    }
}
