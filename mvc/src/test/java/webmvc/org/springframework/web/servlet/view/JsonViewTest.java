package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

    }
    @Test
    void 클래스를_json으로_파싱할_수_있다() throws Exception {
        //given
        final var user = new User("홍길동", 20);
        final var model = Map.of("user", user);

        //when
        final var jsonView = new JsonView();
        final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream outputStream = new BufferedOutputStream(out);
        final PrintWriter printWriter = new PrintWriter(outputStream);
        when(mockResponse.getWriter()).thenReturn(printWriter);

        jsonView.render(model, mock(HttpServletRequest.class), mockResponse);
        printWriter.flush();

        //then
        assertThat(out.toString()).isEqualTo("{\"name\":\"홍길동\",\"age\":20}");
    }

    @Test
    void 여러개_클래스를_json으로_파싱할_수_있다() throws Exception {
        //given
        final var user = new User("홍길동", 20);
        final var model = Map.of("user", user, "other", user);

        //when
        final var jsonView = new JsonView();
        final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStream outputStream = new BufferedOutputStream(out);
        final PrintWriter printWriter = new PrintWriter(outputStream);
        when(mockResponse.getWriter()).thenReturn(printWriter);

        jsonView.render(model, mock(HttpServletRequest.class), mockResponse);
        printWriter.flush();

        //then
        assertThat(out.toString())
                .startsWith("{")
                .contains("\"user\":{\"name\":\"홍길동\",\"age\":20}", "\"other\":{\"name\":\"홍길동\",\"age\":20}")
                .endsWith("}");
    }

}
