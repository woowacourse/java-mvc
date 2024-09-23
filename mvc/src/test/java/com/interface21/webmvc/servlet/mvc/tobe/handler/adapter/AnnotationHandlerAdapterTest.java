package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ExampleController;
import samples.TestController;

class AnnotationHandlerAdapterTest {

    private final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

    @DisplayName("HandlerExecution 타입을 지원한다.")
    @Test
    void supportsHandlerExecution() throws Exception {
        Object controller = new ExampleController();
        Method method = controller.getClass()
                .getMethod("method1", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        assertTrue(handlerAdapter.supports(handlerExecution));
    }

    @DisplayName("HandlerExecution 타입이 아닌 경우 지원하지 않는다.")
    @Test
    void doNotSupport() {
        assertFalse(handlerAdapter.supports(new ExampleController()));
    }

    @DisplayName("요청과 응답을 처리한다.")
    @Test
    void handle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object controller = new TestController();
        Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
