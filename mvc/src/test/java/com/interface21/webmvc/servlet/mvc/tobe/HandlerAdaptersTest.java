package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdaptersTest {

    @DisplayName("handler에 맞는 handlerAdapter가 존재하는 경우 반환")
    @Test
    void adapter_findHandlerAdapter() throws Exception {
        //given
        HandlerAdapter mockHandlerAdapter = mock(HandlerAdapter.class);
        Object handler = new Object();
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(mockHandlerAdapter);

        //when
        when(mockHandlerAdapter.isSupport(handler)).thenReturn(true);
        HandlerAdapter handlerAdapter = handlerAdapters.findHandlerAdapter(handler);

        //then
        assertThat(handlerAdapter).isEqualTo(mockHandlerAdapter);
    }

    @DisplayName("handler에 맞는 handlerAdapter가 없는 경우 예외")
    @Test
    void adapter_notFound() {
        //given
        Object handler = new Object();
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        //when & then
        assertThatThrownBy(() -> handlerAdapters.findHandlerAdapter(handler))
                .isInstanceOf(ServletException.class)
                .hasMessage("No handlerAdapter found");
    }
}