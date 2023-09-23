package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewTest {

    @Test
    void Model_에_맞는_JSON_을_반환할_수_있다() throws Exception {
        // given
        Map<String, String> model = new LinkedHashMap<>();
        model.put("name", "teo");
        model.put("age", "25");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        FakeOutputStream outputStream = new FakeOutputStream();
        when(response.getOutputStream()).thenReturn(outputStream);

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        String content = outputStream.getContent();

        assertThat(content).isEqualTo(
                "{" + "\r\n" +
                        "\t" + "name: teo" + "\r\n" +
                        "\t" + "age: 25" + "\r\n" +
                        "}"
        );
    }

    static class FakeOutputStream extends ServletOutputStream {

        private String content;

        @Override
        public void write(byte[] b) {
            this.content = new String(b);
        }

        public String getContent() {
            return this.content;
        }

        @Override
        public void write(int b) {
            // NO-OP
        }

        @Override
        public void flush() {
            // NO-OP
        }

        @Override
        public void close() {
            // NO-OP
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // NO-OP
        }

        @Override
        public boolean isReady() {
            return false;
        }
    }
}
