package nextstep.mvc.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TUser;

class JsonViewTest {

    @Test
    @DisplayName("Object를 JsonView로 랜더링 해준다.")
    void objectIsReturnedChangedToJsonView() throws IOException {
        // given
        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("key1", "value1");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // when & then
        assertDoesNotThrow(() -> view.render(model, request, response));
    }

    @Test
    @DisplayName("Model의 값을 직렬화 할 수 있다.")
    void canSerializeOfModel() throws Exception {
        // given
        View view = new JsonView();
        Map<String, TUser> model = new HashMap<>();
        model.put("user", new TUser(1L, "rookie"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        view.render(model, request, response);

        // then
        verify(printWriter).write("{\"user\":{"
            + "\"id\":1,"
            + "\"account\":\"rookie\""
            + "}}");
    }
}
