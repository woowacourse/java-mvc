package com.techcourse;

import com.techcourse.adapter.HandlerAdapter;
import com.techcourse.adapter.HandlerAdapterRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerAdapter handlerAdapter1;
    private HandlerAdapter handlerAdapter2;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapter1 = mock(HandlerAdapter.class);
        handlerAdapter2 = mock(HandlerAdapter.class);
    }

    @DisplayName("핸들러 어뎁터 반환 성공 : 핸들러 어뎁터가 1개 있을 때")
    @Test
    void getHandlerAdapter_whenOneExist() {
        // given
        Object handler = new Object();
        when(handlerAdapter1.supports(handler)).thenReturn(true);

        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter1);

        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        Assertions.assertThat(handlerAdapter).isEqualTo(handlerAdapter1);
    }

    @DisplayName("핸들러 어뎁터 반환 성공 : 핸들러 어뎁터가 여러개 있을 때")
    @Test
    void getHandlerAdapter() {
        // given
        Object handler = new Object();
        when(handlerAdapter1.supports(handler)).thenReturn(false);
        when(handlerAdapter2.supports(handler)).thenReturn(true);

        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter1);
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter2);
        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        Assertions.assertThat(handlerAdapter).isEqualTo(handlerAdapter2);
    }

    @DisplayName("핸들러 어뎁터 반환 실패 : 일치하는 핸들러 어뎁터가 존재하지 않을 때")
    @Test
    void noHandlerAdapter() {
        // given
        Object handler = new Object();
        when(handlerAdapter1.supports(handler)).thenReturn(false);

        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter1);

        // when & then
        Assertions.assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class);
    }
}
