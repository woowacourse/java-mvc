package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HandlerExecutionTest {

    @ParameterizedTest
    @MethodSource("handlerExceptions")
    void propagatesHandlerExceptionsWithoutReflectionWrapper(final Exception expected) throws Exception {
        final var execution = execution(expected);

        assertThatThrownBy(() -> execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isSameAs(expected);
    }

    static Stream<Exception> handlerExceptions() {
        return Stream.of(
                new IOException("handler failed"),
                new ServletException("handler failed"),
                new IllegalStateException("handler failed"),
                new ReflectiveOperationException("handler failed"),
                new InvocationTargetException(new IOException("intentionally wrapped"))
        );
    }

    @Test
    void propagatesHandlerErrorsWithoutReflectionWrapper() throws Exception {
        final var expected = new AssertionError("handler failed");
        final var execution = execution(expected);

        assertThatThrownBy(() -> execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isSameAs(expected);
    }

    @Test
    void stillRejectsNullModelAndView() throws Exception {
        final var execution = execution(null);

        assertThatThrownBy(() -> execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ModelAndView를 반환하지 않았습니다");
    }

    private HandlerExecution execution(final Throwable failure) throws Exception {
        final var method = ThrowingController.class.getMethod("handle", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(new ThrowingController(failure), method);
    }

    public static class ThrowingController {

        private final Throwable failure;

        public ThrowingController(final Throwable failure) {
            this.failure = failure;
        }

        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Throwable {
            if (failure != null) {
                throw failure;
            }
            return null;
        }
    }
}
