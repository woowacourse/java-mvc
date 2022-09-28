package nextstep.mvc.view.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.stream.Stream;
import nextstep.mvc.view.View;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.User;

class JspViewResolverTest {

    @MethodSource("jspViewResolver_resolve")
    @ParameterizedTest(name = "JspViewResolver의 resolve메서드는 {0}")
    void sampleTest(String description, Object view, boolean expected) {
        // given
        final JspViewResolver jspViewResolver = new JspViewResolver();

        // when
        final View resolve = jspViewResolver.resolve(view);
        final boolean returnValueIsNull = Objects.nonNull(resolve);

        // then
        assertThat(returnValueIsNull).isEqualTo(expected);
    }

    private static Stream<Arguments> jspViewResolver_resolve() {
        return Stream.of(
                Arguments.of("view가 null일 경우 null을 반환한다", null, false),
                Arguments.of("view가 redirect: 로 시작하지 않고 .jsp로 끝나지 않는 String일 경우 null을 반환한다", "index", false),
                Arguments.of("view가 객체일 경우 null을 반환한다", new User(1L, "gugu", "hkkang@woowahan.com"), false),
                Arguments.of("view가 redirect:로 시작하는 String일 경우 JspView를 반환한다", "redirect:/index.jsp", true),
                Arguments.of("view가 .jsp로 끝나는 String일 경우 JspView를 반환한다", "index.jsp", true)
        );
    }
}
