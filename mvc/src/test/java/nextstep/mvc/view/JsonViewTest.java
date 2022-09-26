package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("model 데이터가 1개일 때 render 하는 경우 value만 반환한다.")
    @Test
    void render() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final View jsonView = new JsonView();
        final Map<String, Object> model = new HashMap<>();
        model.put("user", new User("east"));
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);

        final String expected = objectMapper.writeValueAsString(model.get("user"));
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    @DisplayName("model 데이터가 2개 이상일 때 model 자체를 JSON으로 변환하여 반환")
    @Test
    void render_ManyData() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final View jsonView = new JsonView();
        final Map<String, Object> model = new HashMap<>();
        model.put("user", new User("east"));
        model.put("board", new Board("free"));
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);

        final String expected = objectMapper.writeValueAsString(model);
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    @DisplayName("model 데이터가 없으면 빈 문자열을 JSON으로 반환한다.")
    @Test
    void render_Exception_No_Data() throws Exception{
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final View jsonView = new JsonView();
        final Map<String, Object> model = new HashMap<>();
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);

        final String expected = objectMapper.writeValueAsString(model);
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    static class User {
        private final String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class Board {
        private final String name;

        public Board(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
