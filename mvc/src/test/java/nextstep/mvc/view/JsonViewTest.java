package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import javax.annotation.Nonnull;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("단일 데이터일 경우 depth가 0인 json 데이터를 response에 담는다")
    @Test
    void renderWithSingleData() throws Exception {
        JsonView jsonView = new JsonView();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestWriter writer = new TestWriter();
        given(response.getWriter()).willReturn(writer);

        User user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        jsonView.render(Map.of("user", user), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.getInput()).isEqualTo(
                "{\"id\":1,\"account\":\"gugu\",\"password\":\"password\",\"email\":\"hkkang@woowahan.com\"}");
    }

    @DisplayName("데이터가 1개 이상일 경우 depth가 있는 json 데이터를 response에 담는다")
    @Test
    void render() throws Exception {
        JsonView jsonView = new JsonView();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestWriter writer = new TestWriter();
        given(response.getWriter()).willReturn(writer);

        User gugu = new User(1, "gugu", "password", "hkkang@woowahan.com");
        User lala = new User(2, "lala", "password", "lala@woowahan.com");
        jsonView.render(Map.of("gugu", gugu, "lala", lala), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.getInput()).isEqualTo(
                "{\"lala\":{\"id\":2,\"account\":\"lala\",\"password\":\"password\",\"email\":\"lala@woowahan.com\"},"
                        + "\"gugu\":{\"id\":1,\"account\":\"gugu\",\"password\":\"password\",\"email\":\"hkkang@woowahan.com\"}}");
    }

    static class TestWriter extends PrintWriter {

        private String input;

        public TestWriter() {
            super(System.out);
        }

        @Override
        public void write(@Nonnull final String input) {
            this.input = input;
            super.write(input);
        }

        public String getInput() {
            return input;
        }
    }

    static class User {

        private final long id;
        private final String account;
        private final String password;
        private final String email;

        public User(long id, String account, String password, String email) {
            this.id = id;
            this.account = account;
            this.password = password;
            this.email = email;
        }

        public long getId() {
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