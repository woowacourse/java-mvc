package nextstep.mvc.view;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import samples.TestUser;

public class JsonViewTest {

    @Test
    void render_WhenEmptyModel() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();

        // when
        jsonView.render(model, request, response);
        PrintWriter printWriter = response.getWriter();

        // then
        verify(printWriter).write("{}");
    }

    @Test
    void render_WhenModelSizeOne() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("test", new TestUser(1, "gugu", "1234", "test@test.com"));
        // when
        jsonView.render(model, request, response);
        PrintWriter printWriter = response.getWriter();

        // then
        verify(printWriter).write("{\"id\":1,\"account\":\"gugu\",\"email\":\"test@test.com\"}");
    }

    @Test
    void render_WhenModels() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("test", new TestUser(1, "gugu", "1234", "test@test.com"));
        model.put("key", "value");

        // when
        jsonView.render(model, request, response);
        PrintWriter printWriter = response.getWriter();

        // then
        verify(printWriter).write(
                "{\"test\":{\"id\":1,\"account\":\"gugu\",\"email\":\"test@test.com\"},\"key\":\"value\"}");
    }
}
