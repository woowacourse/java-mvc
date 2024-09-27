package com.interface21.webmvc.servlet.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import samples.TestController;

class HandlerExecutionHandlerAdapterTest {

    private HandlerExecutionHandlerAdapter adapter;
    private HandlerExecution handlerExecution;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() throws Exception {
        adapter = new HandlerExecutionHandlerAdapter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        Method method = TestController.getFindUserIdMethod();
        Object instance = new TestController();
        handlerExecution = new HandlerExecution(instance, method);
    }

    @Test
    void canHandle_whenHandlerIsHandlerExecution_shouldReturnTrue() {
        Object handler = handlerExecution;

        boolean result = adapter.canHandle(handler);

        assertThat(result).isTrue();
    }

    @Test
    void canHandle_whenHandlerIsNotHandlerExecution_shouldReturnFalse() {
        Object handler = new Object();

        boolean result = adapter.canHandle(handler);

        assertThat(result).isFalse();
    }

    @Test
    void handle_shouldReturnModelAndView() throws Exception {
        View expectedView = new JspView("");

        ModelAndView modelAndView = adapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getView()).isEqualTo(expectedView);
    }
}
