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
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void model에_들어간_값이_1개면_해당_object를_바로_JSON으로_반환한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new HashMap<>();
        Object attribute = Map.of("name", "attribute");
        model.put("attribute", attribute);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(attribute));
    }

    @Test
    void model에_들어간_값이_2개_이상이면_model_map을_JSON으로_반환한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new HashMap<>();
        Object attribute1 = Map.of("name", "attribute1");
        Object attribute2 = Map.of("name", "attribute2");
        model.put("attribute1", attribute1);
        model.put("attribute2", attribute2);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(model));
    }

    @Test
    void model이_비어있으면_빈_문자열을_반환한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new HashMap<>();

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("");
    }
}
