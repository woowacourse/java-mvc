package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    public static Stream<Arguments> provideObjWithBool() {
        return Stream.of(
                Arguments.of(mock(HandlerExecution.class), true),
                Arguments.of("String", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideObjWithBool")
    void HandlerExecution_타입_여부에_따라_boolean_값을_반환한다(final Object object, final boolean expected) {
        // given, when, then
        assertThat(handlerAdapter.supports(object)).isEqualTo(expected);
    }

    @Test
    void testMethodNameHere() throws Exception {
        // given
        final var handlerExecution = mock(HandlerExecution.class);
        given(handlerExecution.handle(any(), any()))
                .willReturn(null);

        // when
        handlerAdapter.handle(any(), any(), handlerExecution);

        // then
        verify(handlerExecution).handle(any(), any());
    }
}
