package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("JsonView 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonViewTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void 모델_크기가_1인_경우_값만_출력한다() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        given(response.getWriter()).willReturn(printWriter);
        final String expected = "baron";

        // when
        jsonView.render(Map.of("name", "baron"), request, response);

        // then
        final String writerBuffer = stringWriter.toString();
        assertThat(writerBuffer).isEqualTo(expected);
    }

    @Test
    void 모델_크기가_여러개인_경우_키와_값을_모두_출력한다() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        given(response.getWriter()).willReturn(printWriter);
        final Set<String> expected = Set.of(
                "\"nameA\":\"baron\"",
                "\"nameB\":\"royce\""
        );

        // when
        jsonView.render(Map.of(
                "nameA", "baron",
                "nameB", "royce"
        ), request, response);

        // then
        final String writerBuffer = stringWriter.toString();
        assertThat(writerBuffer).contains(expected);
    }
}
