package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.UserObject;

class JsonViewTest {

    @Test
    void renderJsonView() throws Exception {
        JsonView jsonView = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> model = new HashMap<>();
        model.put("hi", "hi");
        jsonView.render(model, request, response);

        verify(writer).write("\"hi\"");
    }

    @Test
    void renderJsonViewWithObject() throws Exception {
        JsonView jsonView = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> model = new HashMap<>();
        UserObject userObject = new UserObject(1L, "brorae", "password", "brorae@gmail.com");
        model.put("userObject", userObject);
        jsonView.render(model, request, response);

        verify(writer).write("{\"id\":1,\"account\":\"brorae\",\"password\":\"password\",\"email\":\"brorae@gmail.com\"}");
    }

}
