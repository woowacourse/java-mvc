package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonViewTest {

    @Test
    @DisplayName("단일 model을 반환할 수 있다.")
    void renderOneValue() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, ?> model = Map.of("hi", "zero");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        MyPrintWriter myPrintWriter = new MyPrintWriter();
        given(response.getWriter()).willReturn(myPrintWriter);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(myPrintWriter.written).isEqualTo("zero");
    }

    @Test
    @DisplayName("다중 model을 반환할 수 있다.")
    void renderMultiValue() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, ?> model = Map.of(
                "hello", "zero",
                "hi", "jaeho"
        );
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        MyPrintWriter myPrintWriter = new MyPrintWriter();
        given(response.getWriter()).willReturn(myPrintWriter);
        final String expected = new ObjectMapper().writeValueAsString(model);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(myPrintWriter.written).isEqualTo(expected);
    }

    class MyPrintWriter extends PrintWriter {

        protected String written;

        public MyPrintWriter() {
            super(System.out);
        }

        @Override
        public void write(String s) {
            super.write(s);
            written = s;
        }
    }
}
