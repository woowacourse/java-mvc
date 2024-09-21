package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @Test
    @DisplayName("주어진 method를 reflection으로 실행한다.")
    void handle() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        
        Method method = TestController.class.getMethod("getView", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);

        //when
        ModelAndView modelAndView = handlerExecution.handle(mockRequest, mockResponse);
        JspView jspView = (JspView) modelAndView.getView();

        //then
        assertThat(jspView.getViewName()).isEqualTo("test-view");
    }
}
