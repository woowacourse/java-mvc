package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionAdaptorTest {

    @Test
    @DisplayName("Object 객체 handler를 받아서 결과에 해당하는 ModelAndView를 반환한다.")
    void handle() throws Exception {
        //given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution handler = mock(HandlerExecution.class);

        when(handler.handle(request, response)).thenReturn(new ModelAndView(new JspView("test_view")));

        HandlerExecutionAdaptor handlerExecutionAdaptor = new HandlerExecutionAdaptor();

        //when
        ModelAndView modelAndView = handlerExecutionAdaptor.handle(request, response, handler);

        //then
        assertThat(modelAndView.getView()).isExactlyInstanceOf(JspView.class)
                .hasFieldOrPropertyWithValue("viewName", "test_view");
    }

    @Test
    @DisplayName("Object 객체 handler가 HandlerExecution 클래스에 속하는지 여부를 반환한다.")
    void supports() {
        //given
        Object handler = mock(HandlerExecution.class);

        HandlerExecutionAdaptor handlerExecutionAdaptor = new HandlerExecutionAdaptor();

        //when
        boolean isHandlerExecutionInstance = handlerExecutionAdaptor.supports(handler);

        //then
        assertThat(isHandlerExecutionInstance).isTrue();
    }
}
