package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingHandlerAdapterTest {
    private final RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();

    @DisplayName("HandlerExecution 타입의 핸들러를 지원한다.")
    @Test
    void supportHandlerExecution() {
        final var handler = mock(HandlerExecution.class);

        assertThat(adapter.supports(handler)).isTrue();
    }

    @DisplayName("HandlerExecution 타입이 아닌 핸들러는 지원하지 않는다.")
    @Test
    void notSupportsOtherHandler() {
        final var handler = new ForwardController("/index.jsp");

        assertThat(adapter.supports(handler)).isFalse();
    }

    @DisplayName("HandlerExecution을 실행한 결과를 ModelAndView로 반환한다.")
    @Test
    void handle() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var expected = new ModelAndView(new JspView("/index.jsp"));
        final var handlerExecution = mock(HandlerExecution.class);

        when(handlerExecution.handle(request, response)).thenReturn(expected);

        final var modelAndView = adapter.handle(request, response, handlerExecution);

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView).isSameAs(expected); // 똑같은 ModelAndView 객체인지 확인
    }
}
