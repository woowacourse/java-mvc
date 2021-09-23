package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import nextstep.mvc.controller.util.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HandlerExecutionAdapterTest {

    @DisplayName("파라미터로 받은 handler의 handler 메소드를 호출해 반환한다.")
    @Test
    void handle() throws Exception {
        ModelAndView expected = ModelAndView.ofJsp("test");

        HandlerExecution handler = mock(HandlerExecution.class);
        when(handler.handle(any(), any())).thenReturn(expected);

        HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        ModelAndView actual = handlerExecutionAdapter
            .handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), handler);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("인자로 받은 타입의 Handler를 처리가능 여부를 반환한다.")
    @MethodSource(value = "getHandlers")
    @ParameterizedTest
    void supports(Object handler, boolean expected) {
        HandlerExecutionAdapter adapter = new HandlerExecutionAdapter();

        assertThat(adapter.supports(handler)).isEqualTo(expected);
    }

    private static Stream<Arguments> getHandlers() {
        return Stream.of(
            Arguments.of(mock(HandlerExecution.class), true),
            Arguments.of("String", false),
            Arguments.of(1123, false)
        );
    }
}