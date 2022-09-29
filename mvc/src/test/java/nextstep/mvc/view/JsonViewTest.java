package nextstep.mvc.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("JsonView는 모델을 이용해 ResponseBody에 Json 형식으로 작성한다.")
    void render() throws Exception {
        //given
        final JsonView jsonView = new JsonView();

        Map<String, String> expect = Map.of("account", "me");
        final Map<String, String> model = expect;
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();

        given(response.getWriter())
                .willReturn(new PrintWriter(stringWriter));

        //when
        jsonView.render(model, request, response);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ?> actual = objectMapper.readValue(stringWriter.toString(), Map.class);

        //then
        Assertions.assertThat(actual).isEqualTo(expect);
    }
}
