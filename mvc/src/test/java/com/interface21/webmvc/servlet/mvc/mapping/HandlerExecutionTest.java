package com.interface21.webmvc.servlet.mvc.mapping;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {

    static class TestController {

        private void testMethod() {
        }
    }

    @DisplayName("Controller의 메서드에 접근할 수 없는 경우 예외가 발생한다.")
    @Test
    void handle() {
        // given
        TestController controller = new TestController();
        HandlerExecution handlerExecution = new HandlerExecution(controller,
                controller.getClass().getDeclaredMethods()[0]);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when & then
        assertThatThrownBy(() -> handlerExecution.handle(request, response))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("핸들러 메서드 호출 중 오류가 발생했습니다.");
    }
}
