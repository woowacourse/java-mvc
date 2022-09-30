package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("하나의 attribute를 Json 형태의 응답값으로 반환한다.")
    void render() throws Exception {
        final JsonView jsonView = new JsonView();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        final Map<String, Object> model = new HashMap<>();
        Object attribute = Map.of("test", "attribute");
        model.put("attribute", attribute);

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(attribute));
    }

    @Test
    @DisplayName("두개이상의 attribute를 Json 형태의 응답값으로 반환한다.")
    void render_multipleAttributesOfModel() throws Exception {
        final JsonView jsonView = new JsonView();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        final Map<String, Object> model = new HashMap<>();
        final Object attribute1 = Map.of("test", "attribute");
        final Object attribute2 = Map.of("test", "attribute");
        model.put("attribute1", attribute1);
        model.put("attribute2", attribute2);

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo(objectMapper.writeValueAsString(model));
    }
}
