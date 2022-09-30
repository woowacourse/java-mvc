package nextstep.mvc.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.DataExample;

class JsonViewTest {

    @Test
    @DisplayName("모델에 전달받은 데이터를 JSON 형식으로 Response Body에 담는다.")
    void render() throws Exception {
        // given
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonView jsonView = new JsonView();

        final DataExample data = new DataExample(1L, "example");
        Map<String, ?> model = Map.of("data", data);
        final String expect = objectMapper.writeValueAsString(model);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);
        willDoNothing().given(writer).write(any(String.class));

        // when
        jsonView.render(model, request, response);

        // then
        assertAll(
                () -> verify(response).getWriter(),
                () -> verify(writer).write(expect)
        );
    }
}
