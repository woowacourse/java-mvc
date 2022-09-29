package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JsonViewTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @DisplayName("Json 형식으로 렌더링한다.")
    @ParameterizedTest
    @MethodSource("provideForRender")
    void render(final Map<String, ?> expected) throws Exception {
        // given
        final StringWriter stringWriter = new StringWriter();
        final JsonView jsonView = new JsonView();

        // when
        jsonView.render(expected, mockHttpServletRequest(), mockHttpServletResponse(stringWriter));

        // then
        final String actualString = stringWriter.toString();
        final Map<String, ?> actual = OBJECT_MAPPER.readValue(actualString, Map.class);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForRender() {
        return Stream.of(
                Arguments.of(Map.of("account", "gugu")),
                Arguments.of(Map.of("id", 1, "account", "gugu"))
        );
    }

    private HttpServletRequest mockHttpServletRequest() {
        return mock(HttpServletRequest.class);
    }

    private HttpServletResponse mockHttpServletResponse(final StringWriter stringWriter) throws IOException {
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        return response;
    }
}
