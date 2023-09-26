package webmvc.org.springframework.web.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
    }

    @Test
    void 결과를_Response의_Body에_Json_형식으로_내려준다() throws Exception {
        // given
        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> model = new HashMap<>();
        model.put("apple", "red");
        model.put("banana", "yellow");
        model.put("age", 25);
        String body = new ObjectMapper().writeValueAsString(model);

        // when
        JsonView view = new JsonView();
        view.render(model, request, response);

        // then
        verify(response).setContentType(APPLICATION_JSON_UTF8_VALUE);
        verify(response).getWriter();
        verify(response.getWriter()).write(body);
    }
}
