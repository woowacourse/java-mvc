package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JsonViewTest {

    @DisplayName("Json으로 정상적으로 출력되는지 테스트한다.")
    @Test
    void render() throws Exception {
        JsonView jsonView = new JsonView();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Mockito.when(response.getWriter()).thenReturn(writer);

        jsonView.render(Map.<String, Object>of("hello", "world"), request, response);
        final String expected = "{\n" +
        "  \"hello\" : \"world\"\n" +
        "}";
        
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }
}