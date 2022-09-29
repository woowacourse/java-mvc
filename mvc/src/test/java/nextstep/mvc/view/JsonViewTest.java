package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("HTTP Response Body로 JSON 타입의 데이터를 받았을 때, JSON View로 처리한다.")
    @Test
    void render() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", "sun");

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        // when
        when(response.getWriter()).thenReturn(printWriter);
        modelAndView.render(request, response);

        // then
        final String output = stringWriter.toString();
        assertThat(output.split("\n")[1].strip()).contains("\"user\" : \"sun\"");
    }
}
