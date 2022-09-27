package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("model 을 key-value json 형식으로 반환한다.")
    void render() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of(
                "key-1", "value-1",
                "key-2", "value-2");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DummyPrintWriter writer = new DummyPrintWriter();
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.written).isEqualTo("{\"key-1\":\"value-1\",\"key-2\":\"value-2\"}");
    }

    @Test
    @DisplayName("model에 key-value가 한 쌍이면 값만 반환한다.")
    void renderOneValue() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of("key-1", "value-1");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DummyPrintWriter writer = new DummyPrintWriter();
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.written).isEqualTo("value-1");
    }

    @DisplayName("model이 비어있을 때 빈 객체를 반환한다.")
    @Test
    void render_ifModelHasNoValue() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DummyPrintWriter writer = new DummyPrintWriter();
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.written).isEqualTo("{}");
    }

    static class DummyPrintWriter extends PrintWriter {

        public String written;

        public DummyPrintWriter() {
            super(System.out);
        }

        @Override
        public void write(String s) {
            super.write(s);
            written = s;
        }
    }
}