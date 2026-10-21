package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

    @Test
    @DisplayName("HandlerExecution만 지원한다")
    void supports_handler_execution() throws Exception {
        final var handler = createHandler(new TestController(new ModelAndView(mock(View.class))), "handle");

        assertThat(adapter.supports(handler)).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    @DisplayName("컨트롤러가 반환한 뷰와 모델을 그대로 전달한다")
    void preserve_model_and_view() throws Exception {
        final var result = new ModelAndView(mock(View.class)).addObject("id", "gugu");
        final var handler = createHandler(new TestController(result), "handle");

        final var modelAndView = adapter.handle(mock(HttpServletRequest.class),
                mock(HttpServletResponse.class), handler);

        assertThat(modelAndView).isSameAs(result);
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("리플렉션 실행 실패의 원인을 유지한다")
    void preserve_execution_failure_cause() throws Exception {
        final var handler = createHandler(new TestController(null), "fail");

        assertThatThrownBy(() -> adapter.handle(mock(HttpServletRequest.class),
                mock(HttpServletResponse.class), handler))
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("failed");
    }

    private HandlerExecution createHandler(final TestController controller, final String methodName) throws Exception {
        final var method = TestController.class.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(controller, method);
    }

    public static class TestController {

        private final ModelAndView result;

        public TestController(final ModelAndView result) {
            this.result = result;
        }

        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return result;
        }

        public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
            throw new IllegalStateException("failed");
        }
    }
}
