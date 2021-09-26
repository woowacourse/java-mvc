package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 JSON으로 변환해서 반환한다.")
    void renderWithOneObject() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        final User user = new User(1L, "oz");
        model.put("user", user);
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter.toString())
                .isEqualTo(String.join(System.lineSeparator(), "{",
                        "  \"id\" : " + user.id + ",",
                        "  \"account\" : " + "\"" + user.account + "\"",
                        "}"));
    }

    @Test
    @DisplayName("데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.")
    void renderWithManyObject() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        final User user = new User(1L, "oz");
        final User anotherUser = new User(2L, "hi");
        model.put("user", user);
        model.put("anotherUser", anotherUser);
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter.toString())
                .isEqualTo(String.join(System.lineSeparator(), "{",
                        "  \"anotherUser\" : {",
                        "    \"id\" : 2,",
                        "    \"account\" : \"hi\"",
                        "  },",
                        "  \"user\" : {",
                        "    \"id\" : 1,",
                        "    \"account\" : \"oz\"",
                        "  }",
                        "}"));
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