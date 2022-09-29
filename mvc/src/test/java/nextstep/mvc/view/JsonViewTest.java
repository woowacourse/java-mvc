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
import samples.TestUser;

class JsonViewTest {

    @DisplayName("데이터가 1개일 떄, 응답이 원하는 대로 들어갔는지 읽어본다.")
    @Test
    void render_one_data() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        Map<String, Object> model = new HashMap<>();
        final TestUser azpi = new TestUser("azpi", 27);
        model.put("user", azpi);

        final JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        final String jsonValue = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(azpi);
        assertThat(stringWriter.toString()).isEqualTo(jsonValue);
    }

    @DisplayName("데이터가 여러 개일 떄, 응답이 원하는 대로 들어갔는지 읽어본다.")
    @Test
    void render_many_data() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        Map<String, Object> model = new HashMap<>();
        final TestUser azpi = new TestUser("azpi", 27);
        final TestUser shin = new TestUser("shin", 27);
        model.put("user1", azpi);
        model.put("user2", shin);

        final JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        final String jsonValue = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(model);
        assertThat(stringWriter.toString()).isEqualTo(jsonValue);
    }
}
