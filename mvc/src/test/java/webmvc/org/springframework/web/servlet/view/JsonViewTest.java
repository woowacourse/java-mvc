package webmvc.org.springframework.web.servlet.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.org.springframework.http.MediaType;

class JsonViewTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    @DisplayName("JspView render Test 값이 한개 일 떄")
    void renderTest() throws Exception {
        final JsonView jsonView = new JsonView();
        final Map<String, Object> model = new HashMap<>();
        final String stringObject = "string";
        final PrintWriter mockWriter = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(mockWriter);
        model.put("string object", stringObject);

        //when
        jsonView.render(model, request, response);

        //then
        final String expectedValue = "\"string\"";
        verify(mockWriter, times(1)).write(expectedValue);
        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    @DisplayName("JspView render Test 값이 두개 이상일 떄")
    void renderTest2() throws Exception {
        final JsonView jsonView = new JsonView();
        final Map<String, Object> model = new HashMap<>();
        final PrintWriter mockWriter = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(mockWriter);
        model.put("string object1", "value1");
        model.put("string object2", "value2");

        //when
        jsonView.render(model, request, response);

        //then
        final String expectedValue = String.join(System.lineSeparator(), "{"
            + "\"string object1\":\"value1\","
            + "\"string object2\":\"value2\""
            + "}");
        verify(mockWriter, times(1)).write(expectedValue);
        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
