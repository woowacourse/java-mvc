package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionTest {

    static class HandlerExecutionTestController {

        public HandlerExecutionTestController() {
        }

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            ModelAndView modelAndView = mock(ModelAndView.class);
            when(modelAndView.getObject("test")).thenReturn("test");
            return modelAndView;
        }
    }

    @Test
    @DisplayName("메서드 실행 성공")
    void executeHandler() throws Exception {
        // given
        HandlerExecutionTestController controller = new HandlerExecutionTestController();
        Method method = controller.getClass().getMethod("test", HttpServletRequest.class, HttpServletResponse.class);

        // when
        HandlerExecution handlerExecution = new HandlerExecution(new HandlerExecutionTestController(), method);
        ModelAndView result = handlerExecution.handle(null, null);

        // then
        assertThat(result.getObject("test")).isEqualTo("test");
    }
}
