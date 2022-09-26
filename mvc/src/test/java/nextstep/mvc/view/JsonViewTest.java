package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JsonViewTest {

    @Test
    @DisplayName("Json 형태로 response 값이 기록된다.")
    void render() throws Exception {
        // given
        final View jsonView = new JsonView();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final TestWriter writer = new TestWriter();
        when(response.getWriter()).thenReturn(writer);
        final Map<String, Object> model = new HashMap<>();
        model.put("account", "gugu");

        // when
        jsonView.render(model, request, response);
        final String expected = new ObjectMapper().writeValueAsString(model);

        // then
        verify(response, times(2)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        assertThat(writer.getActual()).isEqualTo(expected);
    }

    static class TestWriter extends PrintWriter {

        private String actual;

        TestWriter() {
            super(System.out);
        }

        @Override
        public void write(@Nonnull final String input) {
            actual = input;
            super.write(input);
        }

        String getActual() {
            return actual;
        }
    }
}
