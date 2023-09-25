package webmvc.org.springframework.web.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.View;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewTest {

    @Test
    void Model의_값이_하나인_경우_value를_그대로_출력한다() throws Exception {
        // given
        final View view = new JsonView();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        // when
        view.render(Map.of("Hello", "World"), request, response);

        // then
        verify(writer, only()).write("World");
    }

    @Test
    void Model의_값이_여러개_인_경우_json_형식으로_출력한다() throws Exception {
        // given
        final View view = new JsonView();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        // when
        view.render(Map.of("Hello", "World", "hi", "bye"), request, response);

        // then
        verify(writer, only()).write(anyString());
    }
}
