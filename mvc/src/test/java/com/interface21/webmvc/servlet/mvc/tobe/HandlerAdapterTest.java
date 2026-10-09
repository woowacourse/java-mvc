package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterTest {

    @Test
    @DisplayName("HandlerExecution을 실행하여 ModelAndView를 반환한다")
    void handlesHandlerExecution() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        TestController controller = new TestController();
        Method method = TestController.class.getDeclaredMethod(
                "allMethods",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("mapping")).isEqualTo("all-methods");
    }

    @Test
    @DisplayName("HandlerExecution만 지원한다")
    void supportsHandlerExecution() throws Exception {
        Method method = TestController.class.getDeclaredMethod(
                "allMethods",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), method);
        HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }
}
