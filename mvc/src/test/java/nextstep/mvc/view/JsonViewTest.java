package nextstep.mvc.view;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestUser;

class JsonViewTest {

    @Test
    void render는_받은_model을_json형식의_view로_응답한다() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printWriter = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(printWriter);

        final TestUser user = new TestUser(1L, "hello");
        final Map<String, TestUser> model = Map.of("user", user);

        // when
        final JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then

        assertAll(
                () -> verify(response).setContentType(APPLICATION_JSON_UTF8_VALUE),
                () -> verify(printWriter).write("{\"user\":{"
                        + "\"id\":1,"
                        + "\"account\":\"hello\""
                        + "}}")
        );
    }
}
