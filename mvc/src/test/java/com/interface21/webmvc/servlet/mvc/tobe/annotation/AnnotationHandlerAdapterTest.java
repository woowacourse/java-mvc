package com.interface21.webmvc.servlet.mvc.tobe.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("어노테이션 핸들러 어댑터는 HandlerExecution 타입의 핸들러를 지원한다.")
    @Test
    void should_returnTrue_when_handlerIsHandlerExecution() {
        // given
        Object handler = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapter.support(handler)).isTrue();
    }

    @DisplayName("어노테이션 핸들러 어댑터는 HandlerExecution 타입이 아닌 핸들러를 지원하지 않는다.")
    @Test
    void should_returnFalse_when_handlerIsNotHandlerExecution() {
        // given
        Object handler = new Object();

        // when & then
        assertThat(handlerAdapter.support(handler)).isFalse();
    }

    @DisplayName("HandlerExecution 타입의 핸들러를 정상적으로 실행한다.")
    @Test
    void should_executeHandler_when_givenHandlerExecution() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("ever");

        Object handler = handlerMapping.getHandler(request);

        // when
        ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("ever");
    }

    @DisplayName("HandlerExecution 타입이 아닌 핸들러 실행을 시도할 경우 예외가 발생한다.")
    @Test
    void should_throwException_when_executeControllerHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");

        Object handler = new Object();

        // when & then
        assertThatThrownBy(() -> handlerAdapter.execute(request, response, handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 핸들러입니다.");
    }
}
