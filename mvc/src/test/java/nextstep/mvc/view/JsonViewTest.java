package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.PrintWriter;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

class JsonViewTest {

    @Test
    @DisplayName("model 안에 있는 데이터를 json 형식으로 반환한다.")
    void render() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of(
            "hello", "world",
            "we are", "woowacourse");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestPrintWriter writer = new TestPrintWriter();
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.written).isEqualTo(new ObjectMapper().writeValueAsString(model));
    }

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    void renderOneValue() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of("hello", "world");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestPrintWriter writer = new TestPrintWriter();
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.written).isEqualTo("world");
    }

    class TestPrintWriter extends PrintWriter {

        public String written;

        public TestPrintWriter() {
            super(System.out);
        }

        @Override
        public void write(String s) {
            super.write(s);
            written = s;
        }

    }

}
