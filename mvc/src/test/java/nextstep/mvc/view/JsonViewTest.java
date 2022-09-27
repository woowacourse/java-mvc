package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> model;
    private Writer writer;
    private JsonView jsonView;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        model = new LinkedHashMap<>();
        writer = new StringWriter();
        jsonView = new JsonView();

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @DisplayName("반환 값이 1개일 떄 올바른 반환을 한다.")
    @Test
    void renderWithModelSizeOne() throws Exception {
        model.put("Object1", new TestObject("Key1", "Value1"));
        jsonView.render(model, request, response);

        assertThat(writer).hasToString("[{\"key\":\"Key1\",\"value\":\"Value1\"}]");
    }

    @DisplayName("반환 값이 2개일 떄 올바른 반환을 한다.")
    @Test
    void renderWithModelSizeTwo() throws Exception {
        model.put("Object1", new TestObject("Key1", "Value1"));
        model.put("Object2", new TestObject("Key2", "Value2"));
        jsonView.render(model, request, response);

        assertThat(writer).hasToString("{\"Object1\":{\"key\":\"Key1\",\"value\":\"Value1\"},"
                + "\"Object2\":{\"key\":\"Key2\",\"value\":\"Value2\"}}");
    }

    class TestObject {

        private String key;
        private String value;

        public TestObject(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
