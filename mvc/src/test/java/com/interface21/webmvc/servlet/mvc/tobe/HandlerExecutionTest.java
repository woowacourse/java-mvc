package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.execution.TestController;

class HandlerExecutionTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("지정된 컨트롤러의 메서드를 실행하고 결과를 ModelAndView로 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        final TestController controller = new TestController();
        final Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        when(request.getAttribute("id")).thenReturn("test-id");

        // when
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("test-id");
    }

    @DisplayName("실행된 메서드에서 예외가 발생하면 그대로 전파한다.")
    @Test
    void handle_throwsException() throws Exception {
        // given
        final TestController controller = new TestController();
        final Method method = controller.getClass()
                .getMethod("throwException", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        // when & then
        assertThatThrownBy(() -> handlerExecution.handle(request, response))
                .hasCauseInstanceOf(IllegalStateException.class);
    }
}
