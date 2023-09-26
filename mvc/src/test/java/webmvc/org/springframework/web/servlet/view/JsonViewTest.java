package webmvc.org.springframework.web.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        printWriter = mock(PrintWriter.class);
    }

    @Test
    void 객체를_받아_값이_한개라면_그대로_응답을_내려준다() throws Exception {
        // given
        final var jsonView = new JsonView();
        final Map<String, Object> attribute = new HashMap<>();
        when(response.getWriter()).thenReturn(printWriter);

        // when
        attribute.put("member", new TestObject("blackcat"));
        jsonView.render(attribute, request, response);

        // then
        verify(printWriter).println("{\"name\":\"blackcat\"}");
    }

    @Test
    void 객체를_받아_값이_2개_이상이라면_JSON_형태로_응답을_내려준다() throws Exception {
        // given
        final var jsonView = new JsonView();
        final Map<String, Object> attribute = new HashMap<>();
        when(response.getWriter()).thenReturn(printWriter);

        // when
        attribute.put("createdAt", "2023-09-25");
        attribute.put("member", new TestObject("blackcat"));
        jsonView.render(attribute, request, response);

        // then
        verify(printWriter).println("{\"createdAt\":\"2023-09-25\",\"member\":{\"name\":\"blackcat\"}}");
    }

    static class TestObject {

        private String name;

        public TestObject(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
