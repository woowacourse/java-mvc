package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("Handler가 HandlerExecution 타입일 때 supports가 true를 반환한다.")
    void supports_returnsTrue() {
        Object handler = mock(HandlerExecution.class);
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

        boolean result = adapter.supports(handler);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Handler가 HandlerExecution 타입이 아닐 때 supports가 false를 반환한다.")
    void supports_returnsFalse() {
        Object handler = new Object();
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

        boolean result = adapter.supports(handler);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("HandlerExecution의 handle을 호출하고 ModelAndView를 반환한다.")
    void handle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ModelAndView expected = new ModelAndView(new JspView("/test.jsp"));
        when(handlerExecution.handle(request, response)).thenReturn(expected);

        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        ModelAndView result = adapter.handle(request, response, handlerExecution);

        assertAll(
                () -> assertThat(result).isEqualTo(expected),
                () -> verify(handlerExecution).handle(request, response)
        );
    }
}
