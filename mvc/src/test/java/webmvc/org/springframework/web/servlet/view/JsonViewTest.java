package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import web.org.springframework.http.MediaType;

import java.io.PrintWriter;
import java.util.Map;

import static org.mockito.Mockito.*;

class JsonViewTest {

    @Test
    void render() throws Exception {
        // given
        final var jsonView = new JsonView();
        final var model = Map.of("key", "value");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
        doNothing().when(writer).write(anyString());

        // when
        jsonView.render(model, request, response);

        // then
        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(writer, times(1)).write(anyString());
    }
}
