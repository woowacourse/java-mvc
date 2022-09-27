package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;

class JsonViewTest {

    @Test
    @DisplayName("데이터가 1개 있을땐 데이터만 출력한다.")
    void render_Single_Data() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        TestWriter testWriter = new TestWriter();
        PrintWriter printWriter = new PrintWriter(testWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new LinkedHashMap<>();
        TestUser user = new TestUser("gugu", "password", "gugu@gmail.com");
        model.put("user", user);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(testWriter.getBuffer()).isEqualTo(
                "{\"account\":\"gugu\",\"password\":\"password\",\"email\":\"gugu@gmail.com\"}");
    }

    @Test
    @DisplayName("데이터가 2개 있을땐 map 형태로 출력한다.")
    void render_Multiple_Data() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        TestWriter testWriter = new TestWriter();
        PrintWriter printWriter = new PrintWriter(testWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new LinkedHashMap<>();
        TestUser gugu = new TestUser("gugu", "password", "gugu@gmail.com");
        TestUser josh = new TestUser("josh", "password", "josh@gmail.com");
        model.put("gugu", gugu);
        model.put("josh", josh);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(testWriter.getBuffer()).isEqualTo(
                "{\"gugu\":{\"account\":\"gugu\",\"password\":\"password\",\"email\":\"gugu@gmail.com\"},\"josh\":{\"account\":\"josh\",\"password\":\"password\",\"email\":\"josh@gmail.com\"}}");
    }

    static class TestWriter extends Writer {

        private String buffer;

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = off; i < len; ++i) {
                stringBuilder.append(cbuf[i]);
            }
            buffer = stringBuilder.toString();
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        public String getBuffer() {
            return buffer;
        }
    }
}
