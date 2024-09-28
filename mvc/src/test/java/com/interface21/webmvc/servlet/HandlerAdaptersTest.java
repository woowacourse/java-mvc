package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    @Test
    @DisplayName("핸들러를 처리할 수 있는 어댑터가 존재하지 않는 경우 예외를 발생한다.")
    void getHandlerAdapter() {
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        Object handler = new Object();

        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("핸들러 어댑터가 존재하지 않습니다. " + handler);
    }
}
