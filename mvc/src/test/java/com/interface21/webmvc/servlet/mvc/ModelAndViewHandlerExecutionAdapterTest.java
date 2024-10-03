package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import samples.TestController;

class ModelAndViewHandlerExecutionAdapterTest {

    @Test
    @DisplayName("ModelAndView를 반환하는 HandlerExecution 타입만 지원한다.")
    void supports() {
        ModelAndViewHandlerExecutionAdapter controllerHandlerAdapter = new ModelAndViewHandlerExecutionAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        Class<ModelAndView> modelAndViewClass = ModelAndView.class;
        OngoingStubbing<Class<?>> ongoingStubbing = when(handlerExecution.getReturnType());
        ongoingStubbing.thenReturn(modelAndViewClass);
        TestController otherController = new TestController();

        Assertions.assertAll(
                () -> assertThat(controllerHandlerAdapter.supports(handlerExecution)).isTrue(),
                () -> assertThat(controllerHandlerAdapter.supports(otherController)).isFalse()
        );
    }
}
