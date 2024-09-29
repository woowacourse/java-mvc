package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestServiceA;
import samples.TestServiceB;

class HandlerExecutionTest {

    private HandlerExecution handlerExecution;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() throws Exception {
        Object controllerClass = new TestController(new TestServiceA(), new TestServiceB());
        Method method = TestController.class.getDeclaredMethod(
                "handleRequest",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        handlerExecution = new HandlerExecution(controllerClass, method);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("Test Controller에 선언된 method를 조회한다.")
    @Test
    void handle() {
        when(request.getRequestURI()).thenReturn("/test");

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView).isNotNull();
        Object message = modelAndView.getModel().get("message");
        assertThat(message).isEqualTo("Hello World");
    }
}
