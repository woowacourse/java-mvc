package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JsonViewTest {

    @Test
    void render() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonView jsonView = new JsonView();

        Map<String, String> model = new HashMap<>();
        model.put("name", "angie");

        when(response.getWriter()).thenReturn(mock(PrintWriter.class));
        jsonView.render(model, request, response);

        String expected = objectMapper.writeValueAsString(model);
        PrintWriter printWriter = response.getWriter();

        verify(printWriter, times(1)).print(expected);
    }
}
