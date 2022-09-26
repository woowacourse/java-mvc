package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("model에 하나의 객체가 있을 때 해당 객체를 json으로 변환하여 응답한다.")
    @Test
    void render_ifModelHasSingleValue() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter())
                .thenReturn(printWriter);

        final TestUser modelValue = new TestUser("복희", 26);
        final Map<String, ?> model = Map.of("user", modelValue);
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(modelValue));
    }

    @DisplayName("model에 둘 이상의 객체가 있을 때 model map을 json 객체로 변환하여 응답한다.")
    @Test
    void render_ifModelHasManyValues() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter())
                .thenReturn(printWriter);

        final TestUser modelValue1 = new TestUser("복희", 26);
        final TestUser modelValue2 = new TestUser("포키", 26);
        final Map<String, ?> model = Map.of(
                "user1", modelValue1, "user2", modelValue2);
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(model));
    }

    @DisplayName("model에 객체가 없을 때 빈 문자열을 응답한다.")
    @Test
    void render_ifModelHasNoValue() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter())
                .thenReturn(printWriter);

        final Map<String, ?> model = Map.of();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("");
    }
}
