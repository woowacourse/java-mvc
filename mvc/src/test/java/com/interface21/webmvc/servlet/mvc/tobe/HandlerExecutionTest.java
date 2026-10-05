package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @ParameterizedTest
    @ValueSource(strings = {"wrongreturn", "privatehandler", "protectedhandler", "packagehandler",
            "wrongcount", "wrongtype", "wrongorder"})
    void rejectsInvalidHandlerDuringInitialization(final String fixture) {
        final var mapping = new AnnotationHandlerMapping("fixtures.signatures." + fixture);

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid handler method:")
                .hasMessageContaining("InvalidController.handle");
    }

    @Test
    void returnsResultOfValidHandler() throws Exception {
        final var controller = new ExecutionController();
        final var execution = execution(controller, "success");

        assertThat(execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isSameAs(controller.result);
    }

    @Test
    void propagatesOriginalControllerExceptionAtExecutionTime() throws Exception {
        final var controller = new ExecutionController();
        final var execution = execution(controller, "failure");

        assertThatThrownBy(() -> execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isSameAs(controller.exception);
    }

    @Test
    void propagatesOriginalControllerErrorAtExecutionTime() throws Exception {
        final var controller = new ExecutionController();
        final var execution = execution(controller, "error");

        assertThatThrownBy(() -> execution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isSameAs(controller.error);
    }

    private HandlerExecution execution(final ExecutionController controller, final String name) throws Exception {
        return new HandlerExecution(controller,
                ExecutionController.class.getMethod(name, HttpServletRequest.class, HttpServletResponse.class));
    }

    public static class ExecutionController {

        private final ModelAndView result = new ModelAndView(new JspView("/test.jsp"));
        private final Exception exception = new Exception("controller failure");
        private final Error error = new AssertionError("controller error");

        public ModelAndView success(final HttpServletRequest request, final HttpServletResponse response) {
            return result;
        }

        public ModelAndView failure(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            throw exception;
        }

        public ModelAndView error(final HttpServletRequest request, final HttpServletResponse response) {
            throw error;
        }
    }
}
