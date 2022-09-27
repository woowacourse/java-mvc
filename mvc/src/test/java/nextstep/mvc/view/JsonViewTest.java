package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("데이터가 하나일 경우, value만 반환한다.")
    @Test
    void render_OneValue() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, Object> model = Map.of("key", "value");
        jsonView.render(model, request, response);

        verify(response.getWriter()).write("\"value\"");
    }

    @DisplayName("데이터가 여러 개일 경우 key와 value의 쌍들을 반환한다.")
    @Test
    void render_MultiMap() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        JsonView jsonView = new JsonView();
        LinkedHashMap<String, Object> model = new LinkedHashMap<>();
        model.put("firstKey", "firstValue");
        model.put("secondKey", "secondValue");

        jsonView.render(model, request, response);

        verify(response.getWriter()).write("{\"firstKey\":\"firstValue\",\"secondKey\":\"secondValue\"}");
    }
}
