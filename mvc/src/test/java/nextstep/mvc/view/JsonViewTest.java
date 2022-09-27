package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
    void render() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        User user = new User(1L, "id", "pw", "email@email.com");
        Map<String, User> model = Map.of("user", user);
        JsonView jsonView = new JsonView();

        jsonView.render(model, request, response);

        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(writer, times(1)).write("{\"user\":" + user + "}");
    }

    static class User {

        public final long id;
        public final String account;
        public final String password;
        public final String email;

        User(long id, String account, String password, String email) {
            this.id = id;
            this.account = account;
            this.password = password;
            this.email = email;
        }

        @Override
        public String toString() {
            return "{\"id\":1,"
                    + "\"account\":\"id\","
                    + "\"password\":\"pw\","
                    + "\"email\":\"email@email.com\"}";
        }
    }
}
