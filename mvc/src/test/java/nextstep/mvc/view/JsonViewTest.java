package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.DummyUser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class JsonViewTest {

    @DisplayName("모델을 Json 형태로 렌더링하여 응답한다.")
    @Test
    void render() throws Exception {
        // given
        String expect =
                "{\"id\":1,\"account\":\"ggyool\",\"password\":\"password\",\"email\":\"ggyool@email.com\"}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter out = new StringWriter();
        PrintWriter printWriter = new PrintWriter(out);
        given(response.getWriter()).willReturn(printWriter);

        Map<String, Object> model = new HashMap<>();
        model.put("user", DummyUser.create());
        JsonView jsonView = new JsonView();

        // when
        jsonView.render(model, request, response);

        // then
        assertThat(out).hasToString(expect);
        then(response).should(times(1))
                .setContentType(APPLICATION_JSON_UTF8_VALUE);
        printWriter.close();
    }
}
