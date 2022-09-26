package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private JsonView jsonView;
    private Map<String, Object> model;
    private Writer writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        jsonView = new JsonView();
        model = new HashMap<>();
        writer = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    void renderOne() throws Exception {
        // given
        final TestUser user = new TestUser("pepper");
        model.put("user", user);

        // when
        jsonView.render(model, request, response);

        // then
        Assertions.assertThat(writer)
                .hasToString("{\"account\":\"pepper\"}");
    }

    @Test
    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환")
    void renderMoreThanOne() throws Exception {
        // given
        final TestUser user = new TestUser("pepper");
        model.put("user1", user);
        model.put("user2", user);

        // when
        jsonView.render(model, request, response);

        // then
        Assertions.assertThat(writer)
                .hasToString("{\n"
                        + "  \"user1\" : {\n"
                        + "    \"account\" : \"pepper\"\n"
                        + "  },\n"
                        + "  \"user2\" : {\n"
                        + "    \"account\" : \"pepper\"\n"
                        + "  }\n"
                        + "}");
    }
}
