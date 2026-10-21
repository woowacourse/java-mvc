package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @Test
    void 컨트롤러가_던진_예외를_감싸지_않고_그대로_던진다() throws Exception {
        final var method = FailingController.class.getDeclaredMethod(
                "fail", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(new FailingController(), method);

        assertThatThrownBy(() -> handlerExecution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("컨트롤러 실패");
    }

    static class FailingController {

        public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
            throw new IllegalStateException("컨트롤러 실패");
        }
    }
}
