package view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonViewTest {

    @Test
    @DisplayName("데이터가 원시값 하나일 때")
    void renderSingleValueTest() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        String user = "크로플";
        model.put("user", user);

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter.toString())
                .isEqualTo("\"" + user + "\"");
    }

    @Test
    @DisplayName("데이터가 여러개의 필드를 가진 객체일 때")
    void renderObjectWithMultiFieldTest() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        final User user = new User(1L, "croffle");
        model.put("user", user);

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter.toString())
                .isEqualTo("{\"id\":" + user.id +
                        ",\"account\":" + "\"" + user.account + "\"}"
                );
    }

    @Test
    @DisplayName("여러개의 객체일 때")
    void renderMultiObjectTest() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        final User user1 = new User(1L, "croffle");
        final User user2 = new User(2L, "oz");
        model.put("user1", user1);
        model.put("user2", user2);

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter.toString())
                .isEqualTo("{" +
                        "\"user1\":" +
                        "{\"id\":" + user1.id +
                        ",\"account\":" + "\"" + user1.account + "\"}" +
                        ",\"user2\":" +
                        "{\"id\":" + user2.id +
                        ",\"account\":" + "\"" + user2.account + "\"}" +
                        "}"
                );
    }

    static class User {
        private final long id;
        private final String account;

        public User(long id, String account) {
            this.id = id;
            this.account = account;
        }

        public long getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }
    }
}
