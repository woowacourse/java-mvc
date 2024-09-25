package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.Handler;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AnnotationHandlerAdapterTest {

    @DisplayName("HandlerExecution 타입을 지원한다.")
    @Test
    void isSupportsTest() {
        // given
        Object handler = mock(HandlerExecution.class);

        // when, then
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        assertThat(handlerAdapter.isSupports(handler)).isTrue();
    }

    @DisplayName("HandlerExecution 타입 외에는 지원하지 않는다.")
    @ParameterizedTest
    @ValueSource(classes = {Handler.class, HandlerAdapter.class, HandlerMapping.class})
    void isSupportsTest1(Class<?> clazz) {
        // given
        Object handler = mock(clazz);

        // when, then
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        assertThat(handlerAdapter.isSupports(handler)).isFalse();
    }

    @DisplayName("어뎁터가 핸들러를 실행하고 ModelAndView를 얻는다.")
    @Test
    void handleTest() throws Exception {
        // given
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

        // when
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        // then
        assertAll(
            () -> assertThat(modelAndView.getView()).isEqualTo(new JspView("")),
            () -> assertThat(modelAndView.getModel().containsKey("id")).isTrue()
        );
    }
}
