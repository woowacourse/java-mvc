package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.dummy.DummyObject;

class JsonViewTest {

    @DisplayName("model에 데이터가 1개 있으면 값을 그대로 반환한다.")
    @Test
    void renderOneModel() throws Exception {
        JsonView jsonView = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        jsonView.render(Map.of("dummy", new DummyObject("dummy", 1000)), request, response);

        verify(writer).write("{\"name\":\"dummy\",\"price\":1000}");
    }

    @DisplayName("model에 데이터가 2개 이상이면 값을 Map형태 그대로 JSON으로 반환한다.")
    @Test
    void renderAnyModel() throws Exception {
        JsonView jsonView = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);
        DummyObject dummyObject1 = new DummyObject("dummy1", 1_000);
        DummyObject dummyObject2 = new DummyObject("dummy2", 10_000);

        when(response.getWriter()).thenReturn(writer);

        jsonView.render(Map.of("test1", dummyObject1, "test2", dummyObject2), request, response);

        verify(writer).write("{\"test2\":{"
                + "\"name\":\"dummy2\","
                + "\"price\":10000"
                + "},"
                + "\"test1\":{"
                + "\"name\":\"dummy1\","
                + "\"price\":1000}}");
    }
}
