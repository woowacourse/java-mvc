package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("어노테이션 메서드 실행 어댑터")
class HandlerExecutionAdapterTest {

    private final HandlerAdapter adapter = new HandlerExecutionAdapter();

    @Nested
    @DisplayName("지원 여부 확인")
    class Supports {

        @Test
        @DisplayName("HandlerExecution 객체를 지원한다")
        void supportsHandlerExecution() {
            // given
            final var execution = mock(HandlerExecution.class);

            // when
            final var supported = adapter.supports(execution);

            // then
            assertThat(supported).isTrue();
        }

        @DisplayName("HandlerExecution이 아닌 실행 대상은 지원하지 않는다")
        @ParameterizedTest(name = "[{index}] 실행 대상={0}")
        @MethodSource("com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapterTest#unsupportedHandlers")
        void rejectsUnsupportedHandler(final String scenario, final Object handler) {
            // when
            final var supported = adapter.supports(handler);

            // then
            assertThat(supported).isFalse();
        }
    }

    @Nested
    @DisplayName("어노테이션 메서드 실행")
    class Handling {

        private HandlerExecution execution;
        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() {
            execution = mock(HandlerExecution.class);
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
        }

        @Test
        @DisplayName("요청·응답을 전달하고 View와 모델이 담긴 ModelAndView를 그대로 반환한다")
        void returnsOriginalModelAndView() throws Exception {
            // given
            final var expected = new ModelAndView(mock(View.class)).addObject("id", "gugu");
            when(execution.handle(request, response)).thenReturn(expected);

            // when
            final var result = adapter.handle(request, response, execution);

            // then
            assertThat(result).isSameAs(expected);
        }

        @Test
        @DisplayName("반환된 View를 어댑터에서 렌더링하지 않는다")
        void doesNotRenderDuringHandling() throws Exception {
            // given
            final var view = mock(View.class);
            when(execution.handle(request, response)).thenReturn(new ModelAndView(view));

            // when
            adapter.handle(request, response, execution);

            // then
            verifyNoInteractions(view);
        }

        @Test
        @DisplayName("실행 대상에서 발생한 예외를 그대로 전달한다")
        void propagatesExecutionFailure() throws Exception {
            // given
            final var failure = new IllegalStateException("execution failed");
            when(execution.handle(request, response)).thenThrow(failure);

            // when & then
            assertThatThrownBy(() -> adapter.handle(request, response, execution))
                    .isSameAs(failure);
        }

        @DisplayName("지원하지 않는 객체의 실행을 요청하면 명확한 오류를 던진다")
        @ParameterizedTest(name = "[{index}] 실행 대상={0}")
        @MethodSource("com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapterTest#unsupportedHandlers")
        void failsForUnsupportedHandler(final String scenario, final Object handler) {
            // when & then
            assertThatThrownBy(() -> adapter.handle(request, response, handler))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("HandlerExecution 타입");
        }
    }

    private static Stream<Arguments> unsupportedHandlers() {
        return Stream.of(
                Arguments.of("기존 Controller 객체", mock(Controller.class)),
                Arguments.of("일반 Object 객체", new Object()),
                Arguments.of("null", null)
        );
    }
}
