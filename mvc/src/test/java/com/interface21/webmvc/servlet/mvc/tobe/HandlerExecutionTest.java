package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import samples.TestController;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("컨트롤러 메서드 실행")
class HandlerExecutionTest {

    @Nested
    @DisplayName("실행 결과")
    class Result {

        @Test
        @DisplayName("요청·응답을 전달하고 반환된 ModelAndView를 그대로 돌려준다")
        void returnsControllerResult() throws Exception {
            // given
            final var controller = mock(TestController.class);
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            final var expected = new ModelAndView(new JspView("/get-test.jsp"));
            when(controller.findUserId(request, response)).thenReturn(expected);
            final var method = TestController.class.getMethod(
                    "findUserId", HttpServletRequest.class, HttpServletResponse.class);
            final var execution = new HandlerExecution(controller, method);

            // when
            final var actual = execution.handle(request, response);

            // then
            assertThat(actual).isSameAs(expected);
        }

        @Test
        @DisplayName("컨트롤러가 null을 반환하면 해당 메서드를 알 수 있는 예외를 던진다")
        void rejectsNullControllerResult() throws Exception {
            // given
            final var controller = mock(TestController.class);
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            when(controller.findUserId(request, response)).thenReturn(null);
            final var method = TestController.class.getMethod(
                    "findUserId", HttpServletRequest.class, HttpServletResponse.class);
            final var execution = new HandlerExecution(controller, method);

            // when & then
            assertThatThrownBy(() -> execution.handle(request, response))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("ModelAndView", TestController.class.getName(), "findUserId");
        }
    }

    @Nested
    @DisplayName("실행 실패")
    class Failure {

        @DisplayName("실패한 메서드 정보와 컨트롤러의 실제 예외를 보존한다")
        @ParameterizedTest(name = "[{index}] 컨트롤러 예외={0}")
        @MethodSource("com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionTest#controllerFailures")
        void preservesControllerFailure(final String scenario, final Exception failure) throws Exception {
            // given
            final var controller = new FailingController(failure);
            final var method = FailingController.class.getMethod(
                    "execute", HttpServletRequest.class, HttpServletResponse.class);
            final var execution = new HandlerExecution(controller, method);
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            // when & then
            assertThatThrownBy(() -> execution.handle(request, response))
                    .isInstanceOf(IllegalStateException.class)
                    .hasCause(failure)
                    .hasMessageContaining(FailingController.class.getName(), "execute");
        }

        @Test
        @DisplayName("컨트롤러의 Error는 일반 실행 예외로 감싸지 않는다")
        void propagatesControllerError() throws Exception {
            // given
            final var failure = new AssertionError("controller error");
            final var controller = new FailingController(failure);
            final var method = FailingController.class.getMethod(
                    "execute", HttpServletRequest.class, HttpServletResponse.class);
            final var execution = new HandlerExecution(controller, method);
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            // when & then
            assertThatThrownBy(() -> execution.handle(request, response))
                    .isSameAs(failure);
        }

        @Test
        @DisplayName("메서드 접근에 실패하면 대상 정보와 접근 예외를 보존한다")
        void describesInaccessibleMethod() throws Exception {
            // given
            final var controller = new PrivateMethodController();
            final var method = PrivateMethodController.class.getDeclaredMethod(
                    "hidden", HttpServletRequest.class, HttpServletResponse.class);
            final var execution = new HandlerExecution(controller, method);
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            // when & then
            assertThatThrownBy(() -> execution.handle(request, response))
                    .isInstanceOf(IllegalStateException.class)
                    .hasCauseInstanceOf(IllegalAccessException.class)
                    .hasMessageContaining(PrivateMethodController.class.getName(), "hidden");
        }
    }

    private static Stream<Arguments> controllerFailures() {
        return Stream.of(
                Arguments.of("런타임 예외", new IllegalArgumentException("invalid controller state")),
                Arguments.of("체크 예외", new IOException("controller read failed"))
        );
    }

    public static class FailingController {

        private final Throwable failure;

        public FailingController(final Throwable failure) {
            this.failure = failure;
        }

        public ModelAndView execute(
                final HttpServletRequest request, final HttpServletResponse response) throws Throwable {
            throw failure;
        }
    }

    public static class PrivateMethodController {

        private ModelAndView hidden(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }
    }
}
