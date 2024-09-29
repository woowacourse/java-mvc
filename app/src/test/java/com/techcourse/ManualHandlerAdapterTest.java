package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    private ManualHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ManualHandlerAdapter();
    }

    @DisplayName("수동 핸들러 어댑터는 Controller 타입의 핸들러를 지원한다.")
    @Test
    void should_returnTrue_when_handlerIsController() {
        // given
        Object handler = new ForwardController("");

        // when & then
        assertThat(handlerAdapter.support(handler)).isTrue();
    }

    @DisplayName("어노테이션 핸들러 어댑터는 HandlerExecution 타입의 핸들러를 지원하지 않는다.")
    @Test
    void should_returnFalse_when_handlerIsHandlerExecution() {
        // given
        Object handler = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapter.support(handler)).isFalse();
    }

    @DisplayName("Controller 타입의 핸들러를 정상적으로 실행한다.")
    @Test
    void should_executeHandler_when_givenController() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/");

        // when
        Object handler = new ForwardController("viewName");
        ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);

        // then
        assertThat(modelAndView.getView()).isSameAs(JspView.from("viewName"));
    }

    @DisplayName("HandlerExecution 타입의 핸들러 실행을 시도할 경우 예외가 발생한다.")
    @Test
    void should_throwException_when_executeHandlerExecution() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = new HandlerExecution(null, null);

        // when & then
        assertThatThrownBy(() -> handlerAdapter.execute(request, response, handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 핸들러입니다.");
    }
}
