package nextstep.mvc.view;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.Test;

public class JsonViewTest {

    @Test
    void model_의_값들을_JSON_형식으로_반환한다() throws Exception {
        // given
        final Dummy dummy = new Dummy("애쉬", 26);

        final Map<String, Object> model = Map.of("dummy", dummy);
        final View jsonView = new JsonView();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printWriter = mock(PrintWriter.class);

        willReturn(printWriter)
                .given(response)
                .getWriter();

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(printWriter).write("{\"dummy\":{\"name\":\"애쉬\",\"age\":26}}");
    }

    private class Dummy {

        private final String name;
        private final int age;

        public Dummy(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}
