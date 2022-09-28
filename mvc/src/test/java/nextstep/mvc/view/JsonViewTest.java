package nextstep.mvc.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void render_메서드를_실행하면_write를_실행한다() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        jsonView.render(new HashMap<>(), request, response);

        // then
        verify(printWriter).write(anyString());
    }
}
