package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("json형태로 반환하는 것을 확인한다")
    void render() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        JsonView jsonView = new JsonView();
        // when
        when(response.getWriter()).thenReturn(writer);
        jsonView.render(Map.of("object", new TestObject("tf1", "tf2")), request, response);

        // then
        verify(writer).write("{\"object\":{\"field1\":\"tf1\",\"field2\":\"tf2\"}}");
    }

    static class TestObject {

        private String field1;
        private String field2;

        public TestObject(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }
}
