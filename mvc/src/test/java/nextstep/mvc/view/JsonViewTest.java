package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.TestUser;

class JsonViewTest {

    @Test
    @DisplayName("데이터가 1개일 때는 그대로 JSON으로 변환한다")
    void renderWithOnlyOneData() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, TestUser> model = new HashMap<>();
        final TestUser tiki = new TestUser(1L, "tiki");
        model.put("user", tiki);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter printWriter = new PrintWriter(out);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        jsonView.render(model, request, response);

        // then
        response.getWriter().flush();
        String result = out.toString();
        assertThat(result).isEqualTo("{\"id\":1,\"account\":\"tiki\"}");
    }

    @Test
    @DisplayName("값이 2개 이상일 때는 Map 형태 그대로 JSON으로 변환한다")
    void renderWithMap() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, TestUser> model = new HashMap<>();
        final TestUser tiki = new TestUser(1L, "tiki");
        final TestUser klay = new TestUser(2L, "klay");
        model.put("user", tiki);
        model.put("user2", klay);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter printWriter = new PrintWriter(out);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        jsonView.render(model, request, response);

        // then
        response.getWriter().flush();
        String result = out.toString();
        assertThat(result).isEqualTo(
                "{\"user2\":{\"id\":2,\"account\":\"klay\"},\"user\":{\"id\":1,\"account\":\"tiki\"}}");
    }
}
