package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionTest {

    @DisplayName("주어진 요청에 맞는 컨트롤러 메서드를 실행시킨 뒤 결과를 ModelAndView로 반환한다.")
    @Test
    void handle() throws Exception {
        TestController controller = mock(TestController.class);
        Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        ModelAndView expected = new ModelAndView(null);
        when(controller.findUserId(request, response)).thenReturn(expected);

        assertAll(
                () -> assertThat(handlerExecution.handle(request, response)).isEqualTo(expected),
                () -> verify(controller).findUserId(request, response)
        );
    }

    @DisplayName("컨트롤러 메서드가 String을 반환하는 경우 JspView가 포함된 ModelAndView를 반환한다.")
    @Test
    void handleWithViewName() throws Exception {
        TestController controller = mock(TestController.class);
        Method method = TestController.class.getDeclaredMethod("string", HttpServletRequest.class, HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        when(controller.string(request, response)).thenReturn("string");

        ModelAndView modelAndView = handlerExecution.handle(request, response);
        assertAll(
                () -> assertThat(modelAndView).isInstanceOf(ModelAndView.class),
                () -> assertThat(((JspView) modelAndView.getView())).isInstanceOf(JspView.class)
        );
    }
}
