package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import samples.TestController;

class HandlerExecutionTest {

    @Test
    @DisplayName("handle 메서드를 호출하면 ModelAndView 객체를 반환한다.")
    void handle() throws Exception {
        Method method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(method);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
