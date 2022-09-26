package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestObject;

class JsonViewTest {

    @Test
    void model에_데이터가_1개이면_값을_그대로_반환한다() throws IOException {
        // given
        JsonView view = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        // when
        when(response.getWriter()).thenReturn(writer);

        view.render(Map.of("테스트", new TestObject("테스트 객체", "테스트")), request, response);

        // then
        verify(writer).write("{\"field1\":\"테스트 객체\",\"field2\":\"테스트\"}");
    }

    @Test
    void model에_데이터가_2개면_Map_형태_그대로_JSON으로_변환해서_반환한다() throws IOException {
        // given
        JsonView view = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        // when
        when(response.getWriter()).thenReturn(writer);

        view.render(Map.of("테스트1", new TestObject("테스트 객체", "테스트"),
                "테스트2", new TestObject("테스트 객체2", "테스트테스트")), request, response);

        // then
        verify(writer)
                .write("{"
                        + "\"테스트1\":{\"field1\":\"테스트 객체\",\"field2\":\"테스트\"},"
                        + "\"테스트2\":{\"field1\":\"테스트 객체2\",\"field2\":\"테스트테스트\"}"
                        + "}");
    }
}
