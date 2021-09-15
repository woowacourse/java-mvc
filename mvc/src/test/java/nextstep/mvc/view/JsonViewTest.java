package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView jsonView;
    private Map<String, String> model;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        jsonView = new JsonView(HttpServletResponse.SC_OK);
        model = new HashMap<>();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        doNothing().when(response).setStatus(isA(Integer.class));
        doNothing().when(response).setContentType(isA(String.class));

        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @DisplayName("데이터가 1개면 값을 반환")
    @Test
    void renderOneValue() throws Exception {
        model.put("key", "value");
        jsonView.render(model, request, response);

        assertThat(writer).hasToString("\"value\"");
    }

    @DisplayName("데이터가 2개 이상이면 json 형태로 반환")
    @Test
    void renderValues() throws Exception {
        model.put("key", "value");
        model.put("key2", "value2");
        jsonView.render(model, request, response);

        assertThat(writer).hasToString(new ObjectMapper().writeValueAsString(model));
    }
}
