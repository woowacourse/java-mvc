package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JsonViewTest {

    @DisplayName("Response body에 Json 형식으로 데이터를 바인딩한다.")
    @Test
    void render() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        final HashMap<String, Object> model = new HashMap<>();
        model.put("account", "asdf");
        model.put("id", 1L);
        final String expected = "{\"id\":1,\"account\":\"asdf\"}";

        given(response.getContentType())
                .willReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);
        given(response.getWriter())
                .willReturn(printWriter);

        // when
        jsonView.render(model, request, response);

        // then
        assertAll(
                () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> assertThat(stringWriter.toString()).isEqualTo(expected)
        );
    }
}
