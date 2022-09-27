package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.RequestMappingHandlerAdapter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestMappingHandlerAdapterTest {

    @MethodSource("support_methodSource")
    @ParameterizedTest(name = "{0}")
    void requestMappingHandlerAdapter_supports_handlerExecution_type(String description, Object handler,
                                                                     boolean expected) {
        // given
        final RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();

        // when
        final boolean actual = requestMappingHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> support_methodSource() {
        return Stream.of(
                Arguments.of(
                        "RequestMappingHandlerAdapter는 HandlerExecution을 support한다",
                        new HandlerExecution(null, null),
                        true
                ),
                Arguments.of(
                        "RequestMappingHandlerAdapter는 HandlerExecution 외엔 support 하지 않는다",
                        null,
                        false
                )
        );
    }
}
