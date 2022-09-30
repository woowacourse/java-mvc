package nextstep.mvc.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void jsonView를_랜더링한다() throws Exception {
        JsonView jsonView = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        doNothing().when(writer).write(anyString());

        jsonView.render(Map.of("user", "user"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response.getWriter()).write(anyString());
    }
}
