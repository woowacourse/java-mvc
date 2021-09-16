package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {
    @Test
    @DisplayName("model 하나일 때 값으로 렌더링")
    void render() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> model = new HashMap<>();
        model.put("user", "gugu");

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        String actual = stringWriter.toString();
        assertThat(actual).isEqualTo("\"gugu\"");
    }

    @Test
    @DisplayName("model 여러개일 때 map 자체로 렌더링")
    void renderSeveralModel() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> model = new HashMap<>();
        model.put("user", "gugu");
        model.put("organization", "woo");

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        String actual = stringWriter.toString();
        assertThat(actual).isEqualTo("{\"organization\":\"woo\",\"user\":\"gugu\"}");
    }
}
