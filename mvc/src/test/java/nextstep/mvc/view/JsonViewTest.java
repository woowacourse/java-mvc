package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;

public class JsonViewTest {

    @DisplayName("model에 하나의 데이터만 있는 경우 해당 데이터만 출력한다.")
    @Test
    void render_oneValue_Success() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        TestUser user = new TestUser("User", 10);
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);

        String expected = "{\n"
            + "  \"name\" : \"User\",\n"
            + "  \"age\" : 10\n"
            + "}";

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        assertThat(writer).hasToString(expected);
    }

    @DisplayName("model에 여러 데이터가 있는 경우 해당 map 전체를 출력한다.")
    @Test
    void name() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        TestUser user = new TestUser("User", 10);
        TestUser anotherUser = new TestUser("Another", 19);
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("another", anotherUser);

        String expected = "{\n"
            + "  \"another\" : {\n"
            + "    \"name\" : \"Another\",\n"
            + "    \"age\" : 19\n"
            + "  },\n"
            + "  \"user\" : {\n"
            + "    \"name\" : \"User\",\n"
            + "    \"age\" : 10\n"
            + "  }\n"
            + "}";

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        assertThat(writer).hasToString(expected);
    }
}
