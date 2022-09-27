package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void init() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);
    }

    @DisplayName(value = "값이 여러개인 경우 map을 json으로 변환")
    @Test
    void jsonMultiParsing() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, Object> model = Map.of("user", Map.of("account", "yeonlog"), "campus","seolleung");

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(model));
    }

    @DisplayName(value = "값이 하나인 경우 해당 값만 json으로 변환")
    @Test
    void jsonOneParsing() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, Object> model = Map.of("user", Map.of("account", "yeonlog"));

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(model.get("user")));
    }
}
