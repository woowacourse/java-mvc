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

@DisplayName("JsonView 는 ")
class JsonViewTest {

    @DisplayName("요청한 데이터를 json 타입으로 반환한다.")
    @Test
    void render() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        final HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
        when(mockedResponse.getWriter()).thenReturn(printWriter);
        final Map<String, Integer> model = new HashMap<>();
        model.put("age", 24);

        new JsonView().render(model, mockedRequest, mockedResponse);

        assertThat(stringWriter).hasToString("24");
    }
}
