package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import nextstep.mvc.view.JsonView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("렌더링을 한다. - Model 크기가 1인 경우")
    @Test
    void renderModelWithSizeOne() throws IOException {
        // given
        JsonView jsonView = new JsonView();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter actual = new StringWriter();
        PrintWriter writer = new PrintWriter(actual);

        willDoNothing().given(response).setContentType(anyString());
        given(response.getWriter()).willReturn(writer);

        User user = new User(1L, "dani", "dani", "dani@woowahan.com");
        String expected = "{\"id\":1,\"account\":\"dani\",\"password\":\"dani\",\"email\":\"dani@woowahan.com\"}";

        HashMap<String, Object> model = new HashMap<>();
        model.put("user", user);

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(actual).hasToString(expected);

        verify(response, times(1)).setContentType(anyString());
        verify(response, times(1)).getWriter();
    }

    @DisplayName("렌더링을 한다. - Model 크기가 1 초과인 경우")
    @Test
    void renderModelWithSizeAboveOne() throws IOException {
        // given
        JsonView jsonView = new JsonView();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter actual = new StringWriter();
        PrintWriter writer = new PrintWriter(actual);

        willDoNothing().given(response).setContentType(anyString());
        given(response.getWriter()).willReturn(writer);

        User user1 = new User(1L, "dani", "dani", "dani@woowahan.com");
        User user2 = new User(2L, "dada", "dada", "dada@woowahan.com");
        String expected = "{"
            + "\"user1\":{\"id\":1,\"account\":\"dani\",\"password\":\"dani\",\"email\":\"dani@woowahan.com\"},"
            + "\"user2\":{\"id\":2,\"account\":\"dada\",\"password\":\"dada\",\"email\":\"dada@woowahan.com\"}"
            + "}";

        HashMap<String, Object> model = new HashMap<>();
        model.put("user1", user1);
        model.put("user2", user2);

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(actual).hasToString(expected);

        verify(response, times(1)).setContentType(anyString());
        verify(response, times(1)).getWriter();
    }

    private static class User {

        private final Long id;
        private final String account;
        private final String password;
        private final String email;

        public User(Long id, String account, String password, String email) {
            this.id = id;
            this.account = account;
            this.password = password;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }
}
