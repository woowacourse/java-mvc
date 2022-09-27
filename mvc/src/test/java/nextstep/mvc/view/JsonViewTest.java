package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("모델 값이 하나 뿐일 땐 해당 값을 브라우저에 보여준다.")
    @Test
    void render_oneMap() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        //when
        JsonView jsonView = new JsonView();
        jsonView.render(Map.of("key", "value"), request, response);

        //then
        verify(response.getWriter()).write("\"value\"");
    }

    @DisplayName("모델 값이 여러 개일 땐 Json 포맷으로 브라우저에 보여준다.")
    @Test
    void render_multiMap() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        //when
        JsonView jsonView = new JsonView();
        jsonView.render(Map.of("key", "value","key2","value2"), request, response);

        //then
        verify(response.getWriter()).write("{\"key\":\"value\",\"key2\":\"value2\"}");
    }
}
