package nextstep.mvc.view;

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
    void model을_JSON_으로_반환한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printWriter = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(printWriter);

        final User user = new User(1L, "slow");
        final Map<String, ?> model = Map.of("user", user);
        final View view = new JsonView();
        view.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(printWriter).write("{\"id\":1,\"name\":\"slow\"}");
    }

    private class User {

        private final long id;
        private final String name;

        public User(final long id, final String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
