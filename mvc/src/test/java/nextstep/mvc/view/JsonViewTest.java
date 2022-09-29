package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private static final Map<String, String> USER;

    static {
        USER = new LinkedHashMap<>();
        USER.put("id", "1");
        USER.put("account", "mat");
        USER.put("password", "password");
        USER.put("email", "mat@gmail.com");
    }

    @DisplayName("model에 데이터가 1개인 경우 값을 그대로 반환한다.")
    @Test
    void model에_데이터가_1개인_경우_값을_그래도_반환한다() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("user", USER);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        View jsonView = new JsonView();
        jsonView.render(model, request, response);
        String actual = stringWriter.toString();

        // then
        assertThat(actual).isEqualTo(
                "{\"id\":\"1\",\"account\":\"mat\",\"password\":\"password\",\"email\":\"mat@gmail.com\"}");
    }

    @DisplayName("model에 데이터가 2개 이상인 경우 Map 형태 그대로 반환한다.")
    @Test
    void model에_데이터가_2개_이상인_경우_Map_형태_그대로_반환한다() throws Exception {
        // given
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", USER);
        model.put("count", 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        View jsonView = new JsonView();
        jsonView.render(model, request, response);
        String actual = stringWriter.toString();
        System.out.println(actual);

        // then
        assertThat(actual).isEqualTo(
                "{\"user\":{\"id\":\"1\",\"account\":\"mat\",\"password\":\"password\",\"email\":\"mat@gmail.com\"},\"count\":1}");
    }
}
