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

        public void setName(final String name) {
            this.name = name;
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
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(user);
        Assertions.assertThat(out.toString()).isEqualTo(json);
    }

    @Test
    void 여러개_클래스를_json으로_파싱할_수_있다() throws Exception {
        //given
        final var user = new User("홍길동", 20);
        final var model = Map.of("user", user, "hi", user);

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
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(model);
        Assertions.assertThat(out.toString()).isEqualTo(json);
    }

}
