package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("model이 하나인 경우 값을 그대로 json 반환한다")
    @Test
    void model_one() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final TestWriter testWriter = new TestWriter();
        final PrintWriter printWriter = new PrintWriter(testWriter);

        when(response.getWriter()).thenReturn(printWriter);
        when(response.getContentType()).thenReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final User user = new User(1, "gugu", "password", "gugu@email.com");

        final View view = new JsonView();
        final ModelAndView modelAndView = new ModelAndView(view);

        // when
        modelAndView.addObject("user", user);

        // then
        modelAndView.render(request, response);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(testWriter.getWriteBuffer()).isEqualTo("{\"id\":1,\"account\":\"gugu\",\"email\":\"gugu@email.com\"}");
    }

    @DisplayName("model이 두개 이상인 경우 값을 Map형식을 json으로 변환하여 반환한다")
    @Test
    void model_two() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final TestWriter testWriter = new TestWriter();
        final PrintWriter printWriter = new PrintWriter(testWriter);

        when(response.getWriter()).thenReturn(printWriter);
        when(response.getContentType()).thenReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final User user1 = new User(1, "gugu", "password", "gugu@email.com");
        final User user2 = new User(2, "alien", "password", "alien@email.com");

        final View view = new JsonView();
        final ModelAndView modelAndView = new ModelAndView(view);

        // when
        modelAndView.addObject("user1", user1);
        modelAndView.addObject("user2", user2);

        // then
        modelAndView.render(request, response);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(testWriter.getWriteBuffer()).isEqualTo("{\"user1\":{\"id\":1,\"account\":\"gugu\",\"email\":\"gugu@email.com\"},\"user2\":{\"id\":2,\"account\":\"alien\",\"email\":\"alien@email.com\"}}");
    }

    public class User {

        private final long id;
        private final String account;
        private final String password;
        private final String email;

        public User(final long id, final String account, final String password, final String email) {
            this.id = id;
            this.account = account;
            this.password = password;
            this.email = email;
        }

        public boolean checkPassword(final String password) {
            return this.password.equals(password);
        }

        public long getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", account='" + account + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    private static class TestWriter extends Writer {

        private char[] writeBuffer = new char[0];

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
            final StringBuilder stringBuilder = new StringBuilder(String.valueOf(writeBuffer));
            stringBuilder.append(cbuf, off, len);
            writeBuffer = stringBuilder.toString()
                    .toCharArray();
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        public String getWriteBuffer() {
            return String.valueOf(writeBuffer);
        }
    }
}
