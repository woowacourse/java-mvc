package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class HandlerExecutionTest {

    private final TestController controller = new TestController();

    @Test
    void handle() throws Exception {
        Method method = TestController.class.getMethod(
                "validParametersAndReturn",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        ModelAndView result = handlerExecution.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        assertThat(result.getView()).isInstanceOf(View.class);
    }

    @Test
    void createHandlerExecutionWhenInvalidParameterCount() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("invalidParameterCount");

        assertThatThrownBy(() -> new HandlerExecution(controller, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸들러 메소드의 인자수가 잘못되었습니다.");
    }

    @Test
    void createHandlerExecutionWhenInvalidRequestParameterType() throws NoSuchMethodException {
        Method method = TestController.class.getMethod(
                "invalidRequestParameterType",
                String.class,
                HttpServletResponse.class
        );

        assertThatThrownBy(() -> new HandlerExecution(controller, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸들러 메소드의 첫번째 인자가 HttpServletRequest 타입이 아닙니다.");
    }

    @Test
    void createHandlerExecutionWhenInvalidResponseParameterType() throws NoSuchMethodException {
        Method method = TestController.class.getMethod(
                "invalidResponseParameterType",
                HttpServletRequest.class,
                String.class
        );

        assertThatThrownBy(() -> new HandlerExecution(controller, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸들러 메소드의 두번째 인자가 HttpServletResponse 타입이 아닙니다.");
    }

    @Test
    void createHandlerExecutionWhenInvalidReturnType() throws NoSuchMethodException {
        Method method = TestController.class.getMethod(
                "invalidReturnType",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        assertThatThrownBy(() -> new HandlerExecution(controller, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸들러 메소드의 반환 타입이 ModelAndView 타입이 아닙니다.");
    }

    static class TestController {

        public ModelAndView validParametersAndReturn(HttpServletRequest request, HttpServletResponse response) {
            return new ModelAndView(mock(View.class));
        }

        public void invalidParameterCount() {
        }

        public void invalidRequestParameterType(String request, HttpServletResponse response) {
        }

        public void invalidResponseParameterType(HttpServletRequest request, String response) {
        }

        public String invalidReturnType(HttpServletRequest request, HttpServletResponse response) {
            return "invalid";
        }
    }
}
