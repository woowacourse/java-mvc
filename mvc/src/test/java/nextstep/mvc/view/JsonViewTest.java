package nextstep.mvc.view;

import static org.mockito.BDDMockito.*;

import java.io.PrintWriter;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

class JsonViewTest {

    @Test
    @DisplayName("model 안에 있는 데이터를 json 형식으로 반환한다.")
    void render() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of("hello", "world");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(response.getWriter()).willReturn(new PrintWriter(System.out));

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).getWriter();
    }

}
