package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    @DisplayName("JsonView는 json 형태로 데이터를 응답한다, model에 데이터 한 개 - 성공")
    void renderJsonViewWithOneModel() throws Exception {
        // given
        Map<String, User> model = new HashMap<>();
        User user = new User(1L, "Corgi", "helloCorgi", "ecsimsw@gmail.com");
        model.put("user", user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);
        String actual = stringWriter.toString();

        // then
        assertThat(actual).isEqualTo("{\"user\":{\"account\":\"Corgi\"}}");
    }

    @Test
    @DisplayName("JsonView는 json 형태로 데이터를 응답한다, model에 데이터 두 개 - 성공")
    void renderJsonViewWithTwoModels() throws Exception {
        // given
        Map<String, User> model = new HashMap<>();
        User corgi = new User(1L, "Corgi", "bestReviewer", "ecsimsw@gmail.com");
        User pk = new User(2L, "PK", "bestReviewee", "pkeugine@gmail.com");
        model.put("corgi", corgi);
        model.put("pk", pk);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);
        String actual = stringWriter.toString();

        // then
        assertThat(actual).isEqualTo("{\"pk\":{\"account\":\"PK\"},\"corgi\":{\"account\":\"Corgi\"}}");
    }
}
