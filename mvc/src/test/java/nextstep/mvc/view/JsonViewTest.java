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
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    @Test
    void render_model_size_one() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        Map<String, String> 아스피 = Map.of("account", "아스피", "password", "1234");
        final Map<String, Object> value = Map.of("아스피", 아스피);

        JsonView jsonView = new JsonView();
        jsonView.render(value, request, response);

        assertThat(stringWriter.toString())
                .isEqualTo(OBJECTMAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(아스피));
    }

    @Test
    void render_model_size_several() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        Map<String, String> 아스피 = Map.of("account", "아스피", "password", "1234");
        Map<String, String> 파랑 = Map.of("account", "파랑", "password", "1234");
        final Map<String, Object> value = Map.of("아스피", 아스피, "파랑", 파랑);

        JsonView jsonView = new JsonView();
        jsonView.render(value, request, response);

        assertThat(stringWriter.toString())
                .isEqualTo(OBJECTMAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value));
    }
}
