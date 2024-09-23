package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionTest.class);
    private static final JspView jspView = new JspView("test");

    static class TestViewController {

        public TestViewController() {
        }

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            log.info("test view controller method");
            return new ModelAndView(jspView);
        }
    }

    @Test
    @DisplayName("주어진 method를 reflection으로 실행한다.")
    void handle() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        Method method = TestViewController.class.getMethod("test", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(method);

        //when
        ModelAndView modelAndView = handlerExecution.handle(mockRequest, mockResponse);
        JspView createdJspView = (JspView) modelAndView.getView();

        //then
        assertThat(createdJspView).isEqualTo(jspView);
    }
}
