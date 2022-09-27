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
    @DisplayName("Model의 값을 Json 형식의 view로 리턴한다.")
    void canReturnJsonViewWhenGreaterOneData() throws Exception {
        // given
        View view = new JsonView();
        Map<String, TUser> model = new HashMap<>();
        model.put("루키", new TUser(1L, "루키"));
        model.put("다우", new TUser(2L, "디우"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        view.render(model, request, response);

        // then
        verify(printWriter).write(
            "{"
                + "\"다우\":{\"id\":2,\"account\":\"디우\"},"
                + "\"루키\":{\"id\":1,\"account\":\"루키\"}"
                + "}");
    }

    @Test
    @DisplayName("Model의 값이 1개일 경우 값을 그대로 값을 리턴한다.")
    void canReturnValueWhenOneData() throws Exception {
        // given
        View view = new JsonView();
        Map<String, TUser> model = new HashMap<>();
        model.put("루키", new TUser(1L, "루키"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // when
        view.render(model, request, response);

        // then
        verify(printWriter).write("{\"id\":1,\"account\":\"루키\"}");
    }
}
