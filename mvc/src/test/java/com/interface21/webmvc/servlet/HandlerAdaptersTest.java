package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    @Test
    @DisplayName("주어진 핸들러의 처리를 위임할 수 있는 어댑터를 반환한다.")
    void getHandler() {
        Object handler = new Object();
        HandlerAdapter handlerAdapter = mock(HandlerAdapter.class);
        when(handlerAdapter.supports(any()))
                .thenReturn(true);
        HandlerAdapters handlerAdapters = new HandlerAdapters(handlerAdapter);

        HandlerAdapter actual = handlerAdapters.getHandlerAdapter(handler);

        assertThat(actual).isEqualTo(handlerAdapter);
    }

    @Test
    @DisplayName("핸들러를 처리할 수 있는 어댑터가 존재하지 않는 경우 예외를 발생한다.")
    void getHandlerAdapter() {
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        Object handler = new Object();

        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("핸들러 어댑터가 존재하지 않습니다. " + handler);
    }

    @Test
    @DisplayName("신규 핸들러 어댑터를 추가한다.")
    void appendHandlerAdapter() {
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        Object handler = new Object();
        HandlerAdapter handlerAdapter = mock(HandlerAdapter.class);
        when(handlerAdapter.supports(any()))
                .thenReturn(true);

        handlerAdapters.appendHandlerAdapter(handlerAdapter);

        HandlerAdapter actual = handlerAdapters.getHandlerAdapter(handler);

        assertThat(actual).isEqualTo(handlerAdapter);
    }
}
