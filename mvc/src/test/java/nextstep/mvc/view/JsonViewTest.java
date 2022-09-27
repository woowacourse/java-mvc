package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private JsonView jsonView;
    private Map<String, Object> model;
    private Writer writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        jsonView = new JsonView();
        model = new LinkedHashMap<>();
        writer = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    @DisplayName("데이터 값을 json 형태로 반환한다.")
    void test() throws Exception {
        model.put("user1", new TestUser("어썸오", "잘하고 있어요"));
        model.put("user2", new TestUser("수달도", "잘하고 있어!"));

        jsonView.render(model, request, response);

        Assertions.assertThat(writer)
                .hasToString(
                        "{\"user1\":{\"name\":\"어썸오\",\"pw\":\"잘하고 있어요\"},\"user2\":{\"name\":\"수달도\",\"pw\":\"잘하고 있어!\"}}");
    }

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    void renderOne() throws Exception {
        final TestUser user = new TestUser("공책 짱이다", "터놓고 짱이다");
        model.put("user", user);

        jsonView.render(model, request, response);

        Assertions.assertThat(writer)
                .hasToString("{\"name\":\"공책 짱이다\",\"pw\":\"터놓고 짱이다\"}");
    }

    class TestUser {

        private String name;
        private String pw;

        public TestUser(final String name, final String pw) {
            this.name = name;
            this.pw = pw;
        }

        public String getName() {
            return name;
        }

        public String getPw() {
            return pw;
        }
    }
}
