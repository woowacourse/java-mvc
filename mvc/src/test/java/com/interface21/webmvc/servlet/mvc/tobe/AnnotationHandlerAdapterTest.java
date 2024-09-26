package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @DisplayName("HandlerExecution의 인스턴스이면 true를 반환한다.")
    @Test
    void supports() {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @DisplayName("전달 받은 핸들러를 실행시켜 ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handler = mock(HandlerExecution.class);
        ModelAndView modelAndView = mock(ModelAndView.class);

        when(handler.handle(request, response)).thenReturn(modelAndView);

        assertThat(handlerAdapter.handle(request, response, handler)).isEqualTo(modelAndView);
    }
}
