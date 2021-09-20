package nextstep.mvc.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ViewResolver 테스트 ")
class ViewResolverTest {

    private static final ViewResolver VIEW_RESOLVER = new ViewResolver();

    private static Stream<Arguments> resolveViewName() {
        return Stream.of(
                Arguments.of("hihi", TextView.class),
                Arguments.of("/register.jsp", JspView.class)
        );
    }

    @ParameterizedTest
    @MethodSource
    void resolveViewName(String viewName, Class<?> clazz) {
        //given
        //when
        final View view = VIEW_RESOLVER.resolveViewName(viewName);
        //then
        assertThat(view).isInstanceOf(clazz);
    }
}
