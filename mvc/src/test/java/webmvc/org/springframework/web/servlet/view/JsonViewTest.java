package webmvc.org.springframework.web.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void render할_때_contentType을_JSON_UTF8로_설정한다() throws IOException {
        // given
        JsonView jsonView = new JsonView();
        given(response.getWriter())
                .willReturn(printWriter);

        // when
        jsonView.render(Map.of(), request, response);

        // then
        then(response)
                .should(times(1))
                .setContentType(APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void size가_1개인_model의_값을_JSON으로_적는다() throws IOException {
        // given
        Map<String, String> model = Map.of("test", "value");
        JsonView jsonView = new JsonView();
        given(response.getWriter())
                .willReturn(printWriter);

        // when
        jsonView.render(model, request, response);

        // then
        then(printWriter)
                .should(times(1))
                .write("\"value\"");
    }

    @Test
    void size가_1보다_큰_model의_값을_JSON으로_적는다() throws IOException {
        // given
        Map<String, String> model = Map.of(
                "test1", "value1",
                "test2", "value2"
        );
        JsonView jsonView = new JsonView();
        given(response.getWriter())
                .willReturn(printWriter);

        // when
        jsonView.render(model, request, response);

        // then
        then(printWriter)
                .should(times(1))
                .write(anyString());
    }
}
