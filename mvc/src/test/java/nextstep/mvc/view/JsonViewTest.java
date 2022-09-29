package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Nested
    @DisplayName("render 메소드는")
    class Render {

        @Test
        @DisplayName("model에 데이터가 존재하지 않으면 빈 문자열을 write한다.")
        void success_withNoData() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            when(response.getWriter()).thenReturn(mock(PrintWriter.class));

            final JsonView jsonView = new JsonView();
            Map<String, Object> model = new HashMap<>();

            // when
            jsonView.render(model, request, response);

            // then
            verify(response.getWriter()).write("\"\"");
        }

        @Test
        @DisplayName("model에 데이터가 1개면 해당 데이터의 value만 write한다.")
        void success_withOnlyOneData() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            when(response.getWriter()).thenReturn(mock(PrintWriter.class));

            final JsonView jsonView = new JsonView();
            Map<String, Object> model = new HashMap<>();
            model.put("name", "eve");

            // when
            jsonView.render(model, request, response);

            // then
            verify(response.getWriter()).write("\"eve\"");
        }

        @Test
        @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 write한다.")
        void success_withMoreThanTwoData() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            when(response.getWriter()).thenReturn(mock(PrintWriter.class));

            final JsonView jsonView = new JsonView();
            Map<String, Object> model = new HashMap<>();
            model.put("name", "eve");
            model.put("course", "wooteco");

            // when
            jsonView.render(model, request, response);

            // then
            verify(response.getWriter()).write("{\"name\":\"eve\",\"course\":\"wooteco\"}");
        }
    }
}