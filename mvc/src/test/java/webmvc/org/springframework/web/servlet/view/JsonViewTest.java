package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewTest {

    @Test
    void 받은_모델을_JSONObject로_렌더링한다() throws Exception {
        final var model = Map.of("test-key", "test-value");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        final var writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        JsonView view = new JsonView();
        view.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("{\"test-key\":\"test-value\"}");
    }
}
