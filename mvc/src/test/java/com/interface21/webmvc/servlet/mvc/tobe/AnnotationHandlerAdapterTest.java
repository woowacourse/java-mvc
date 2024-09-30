package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.ManualHandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AnnotationHandlerAdapterTest {

    @DisplayName("HandlerExecution 은 support 할 수 있다.")
    @Test
    void support() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        handlerMapping.getHandler(request);

        // when
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        boolean support = handlerAdapter.support(handlerMapping.getHandler(request));

        //then
        assertThat(support).isTrue();
    }

    @DisplayName("HandlerExecution 이외에는 support 할 수 없다.")
    @Test
    void cannot_support() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        ManualHandlerMapping handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
        handlerMapping.getHandler(request);

        // when
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        boolean support = handlerAdapter.support(handlerMapping.getHandler(request));

        //then
        assertThat(support).isFalse();
    }

    @DisplayName("HandlerExecution을 실행시킨다.")
    @Test
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        handlerMapping.getHandler(request);

        // when
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        Object handler = handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
