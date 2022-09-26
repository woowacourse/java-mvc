package nextstep.mvc.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void 직렬화한_데이터를_Response에_작성한다() throws Exception {
        // given
        final var jsonView = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);
        final Map<String, String> model = Map.of("user", "gugu");
        given(response.getWriter())
                .willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(writer).write("{\"user\":\"gugu\"}");
    }
}
