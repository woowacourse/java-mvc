package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.handleradapter.RequestMappingHandlerAdapter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setUp() {
        handlerAdapters = new HandlerAdapters();
    }

    @DisplayName("핸들러 어댑터는 가능한 어댑터를 찾지 못할 경우 예외를 반환한다.")
    @Test
    void findNotMatchHandlerAdapter() {
        // given
        handlerAdapters.addHandlerAdapter(new RequestMappingHandlerAdapter());
        Object invalidObject = new Object();

        // when & then
        Assertions.assertThatThrownBy(() -> handlerAdapters.findHandlerAdapter(invalidObject))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("처리할 수 없는 요청입니다.");
    }
}
