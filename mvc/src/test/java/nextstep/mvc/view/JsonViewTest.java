package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView jsonView = new JsonView();

    @Test
    void model에_값이_1개이면_값을_그대로_반환한다() throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put("user", "user");

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("\"user\"");
    }

    @Test
    void model에_값이_2개_이상인_경우_model을_반환한다() throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put("name", "name");
        model.put("user", "user");

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("{\"name\":\"name\",\"user\":\"user\"}");
    }
}
