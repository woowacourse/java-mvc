package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JsonViewTest {

    @DisplayName("넘어온 model에 따라 적절한 응답값을 반환한다.")
    @MethodSource("givenModelAndExpectResult")
    @ParameterizedTest
    void return_json_by_model(final Map<String, Object> model, final String expect) throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final OutputStream outputStream = new BufferedOutputStream(byteArrayOutputStream);
        final PrintWriter printWriter = new PrintWriter(outputStream);
        when(response.getWriter()).thenReturn(printWriter);

        final JsonView jsonView = new JsonView();

        // when
        jsonView.render(model, request, response);
        printWriter.flush();

        // then
        assertThat(byteArrayOutputStream.toString()).isEqualTo(expect);
    }

    private static Stream<Arguments> givenModelAndExpectResult() {
        return Stream.of(
            arguments(Collections.emptyMap(), ""),
            arguments(Map.of("member", new Member("멤버1")), "{\"member\":{\"name\":\"멤버1\"}}"),
            arguments(Map.of("member1", new Member("멤버1"), "member2", new Member("멤버2"))
                , "{\"member1\":{\"name\":\"멤버1\"},\"member2\":{\"name\":\"멤버2\"}}")
        );
    }


    static class Member {

        private final String name;

        public Member(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
