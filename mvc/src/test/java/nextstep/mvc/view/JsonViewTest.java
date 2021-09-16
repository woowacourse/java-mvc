package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private static final TestUser USER = new TestUser("jaeseong", "jerry");

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
        model = new HashMap<>();
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void renderOne() throws Exception {
        model.put("user", USER);

        jsonView.render(model, request, response);

        assertThat(writer).hasToString("{\"name\":\"jaeseong\",\"nickname\":\"jerry\"}");
    }

    @DisplayName("model에 데이터가 1개 이상이면 Map 형태 그대로 반환한다.")
    @Test
    void renderOneMore() throws Exception {
        model.put("user1", USER);
        model.put("user2", USER);

        jsonView.render(model, request, response);

        assertThat(writer).hasToString("{\"user1\":{\"name\":\"jaeseong\",\"nickname\":\"jerry\"},\"user2\":{\"name\":\"jaeseong\",\"nickname\":\"jerry\"}}");
    }
}