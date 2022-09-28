package nextstep.mvc.view.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.stream.Stream;
import nextstep.mvc.view.View;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.User;

class JsonViewResolverTest {


    @MethodSource("jsonViewResolver_resolve")
    @ParameterizedTest(name = "JsonViewResolver의 resolve메서드는 {0}")
    void sampleTest(String description, Object view, boolean expected) {
        // given
        final JsonViewResolver jsonViewResolver = new JsonViewResolver();

        // when
        final View resolve = jsonViewResolver.resolve(view);
        final boolean returnValueIsNull = Objects.isNull(resolve);

        // then
        assertThat(returnValueIsNull).isEqualTo(expected);
    }

    private static Stream<Arguments> jsonViewResolver_resolve() {
        return Stream.of(
                Arguments.of("view가 null일 경우 null을 반환한다", null, true),
                Arguments.of("view가 .jsp로 끝나는 String일 경우 null을 반환한다", "index.jsp", true),
                Arguments.of("view가 redirect:로 시작하는 String일 경우 null을 반환한다", "redirect:/index.jsp", true),
                Arguments.of("view가 null이 아니고 redirect:로 시작하거나 .jsp로 끝나는 String이 아니면 JsonView를 반환한다",
                        new User(1L, "gugu", "hkkang@woowahan.com"), false)
        );
    }
}
