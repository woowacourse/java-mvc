package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.PrintWriter;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

class JsonViewTest {

    @Test
    @DisplayName("데이터를 2개 이상이면 json 방식으로 변환한다.")
    void renderJson() throws Exception {
        //given
        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of(
            "test1", "value1",
            "test2", "value2");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestPrintWriter writer = new TestPrintWriter();
        given(response.getWriter()).willReturn(writer);

        //when
        jsonView.render(model, request, response);

        System.out.println(writer.written);
        //then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertAll(
            () -> assertThat(writer.written.contains("\"test1\":\"value1\"")).isTrue(),
        () -> assertThat(writer.written.contains("\"test2\":\"value2\"")).isTrue()
        );
    }

    @Test
    @DisplayName("데이터가 1개면 값을 그대로 반환한다.")
    void renderOneValue() throws Exception {
        //given
        JsonView jsonView = new JsonView();

        Map<String, ?> model = Map.of("test", "value");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestPrintWriter writer = new TestPrintWriter();
        given(response.getWriter()).willReturn(writer);

        //when
        jsonView.render(model, request, response);

        //then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.written).isEqualTo("\"value\"");
    }

    static class TestPrintWriter extends PrintWriter {

        public String written;

        public TestPrintWriter() {
            super(System.out);
        }

        @Override
        public void write(String input) {
            super.write(input);
            written = input;
        }
    }
}
