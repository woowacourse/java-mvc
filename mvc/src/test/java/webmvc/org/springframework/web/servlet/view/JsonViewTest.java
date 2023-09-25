package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
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

        FakeWriter writer = new FakeWriter();
        when(response.getWriter()).thenReturn(writer);

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        Map<String, String> content = new ObjectMapper().readValue(writer.getContent(), HashMap.class);
        assertThat(content).isEqualTo(model);
    }

    static class FakeWriter extends PrintWriter {

        private String content;

        public FakeWriter() {
            super(nullWriter());
        }

        @Override
        public void write(String s) {
            this.content = s;
        }

        public String getContent() {
            return content;
        }
    }
}
