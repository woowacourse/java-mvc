package nextstep.mvc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.techcourse.air.mvc.core.view.JsonView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JsonViewTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    @DisplayName("모델이 하나인 경우")
    void oneModel() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("key", "data");
        JsonView jsonView = new JsonView();

        // when
        doNothing().when(response).setContentType(anyString());
        when(response.getWriter()).thenReturn(writer);
        jsonView.render(model, request, response);
        writer.flush();

        // then
        assertThat(stringWriter.toString()).isEqualTo("\"data\"");
    }

    @Test
    @DisplayName("모델이 둘인 경우")
    void twoModel() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("key1", "data1");
        model.put("key2", "data2");
        JsonView jsonView = new JsonView();

        // when
        doNothing().when(response).setContentType(anyString());
        when(response.getWriter()).thenReturn(writer);
        jsonView.render(model, request, response);
        writer.flush();

        // then
        String expected = "{\"key1\":\"data1\",\"key2\":\"data2\"}";
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

}
