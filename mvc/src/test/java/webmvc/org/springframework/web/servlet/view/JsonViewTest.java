package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final PrintWriter printWriter = mock(PrintWriter.class);

    @DisplayName("JsonView의 작동을 검증한다.")
    @Test
    void verify_json_view_render() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, String> model = Map.of();

        when(res.getWriter()).thenReturn(printWriter);
        doNothing().when(printWriter).write(anyString());

        // when
        jsonView.render(model, req, res);

        // then
        verify(printWriter).write(anyString());
    }
}
