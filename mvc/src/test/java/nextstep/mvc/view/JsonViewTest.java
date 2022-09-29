package nextstep.mvc.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        this.jsonView = new JsonView();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.writer = mock(PrintWriter.class);
    }

    @Test
    void 직렬화할_데이터가_두_개_이상이면_Map_형태로_Response에_작성한다() throws Exception {
        // given
        final Map<String, ?> model = new LinkedHashMap<>(Map.of("name", "gugu", "age", 20));
        given(response.getWriter())
                .willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(writer).write("{\"name\":\"gugu\",\"age\":20}");
    }

    @Test
    void 직렬화할_데이터가_한_개라면_값을_그대로_Response에_작성한다() throws Exception {
        // given
        final Map<String, ?> model = Map.of("name", "gugu");
        given(response.getWriter())
                .willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(writer).write("\"gugu\"");
    }
}
