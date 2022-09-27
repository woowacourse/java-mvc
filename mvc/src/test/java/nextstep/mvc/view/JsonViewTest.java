package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonView jsonView = new JsonView();

    @Test
    @DisplayName("json 객체의 모델 데이터가 1개일 때 값을 그대로 반환한다.")
    void renderWithSingularObject() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        final Map<String, String> expected = Map.of("parang", "koparang");
        when(response.getWriter()).thenReturn(printWriter);

        final Map<String, Object> model = new HashMap<>();
        model.put("wooteco", expected);

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @Test
    @DisplayName("json 객체의 모델 데이터가 2개일 때 값을 그대로 반환한다.")
    void renderWithPluralObject() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        final Map<String, Object> model = new HashMap<>();
        model.put("wooteco", Map.of("parang", "koparang", "k", "kobaby"));
        model.put("parang", Map.of("kotlin", 10, "alcohol", 9));

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(model));
    }
}
