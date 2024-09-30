package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.servlet.HandlerAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

class HandlerAdaptorRegistryTest {

    HandlerAdaptorRegistry handlerAdaptorRegistry;
    HandlerAdaptor handlerAdaptor;

    @BeforeEach
    void setUp() {
        handlerAdaptorRegistry = new HandlerAdaptorRegistry();
        handlerAdaptor = new HandlerExecutionAdaptor();

        handlerAdaptorRegistry.addHandlerAdaptors(handlerAdaptor);
    }

    @Test
    @DisplayName("HandlerAdaptorRegistry에 해당 handlerAdaptor를 등록한다.")
    void addHandlerAdaptor() throws NoSuchFieldException, IllegalAccessException {
        //given
        HandlerAdaptor handlerAdaptor = mock(HandlerExecutionAdaptor.class);

        //when
        handlerAdaptorRegistry.addHandlerAdaptors(handlerAdaptor);
        Field field = handlerAdaptorRegistry.getClass().getDeclaredField("handlerAdaptors");
        field.setAccessible(true);

        List<HandlerAdaptor> handlerAdaptors = (List<HandlerAdaptor>) field.get(handlerAdaptorRegistry);

        //then
        assertAll(
                () -> assertThat(handlerAdaptors).hasSize(2),
                () -> assertThat(handlerAdaptors.getLast()).isEqualTo(handlerAdaptor)
        );
    }

    @Test
    @DisplayName("HandlerAdaptorRegistry에서 해당 handler를 처리할 수 있는 handlerAdaptor를 찾아온다.")
    void getHandlerAdaptor() {
        //given
        Object handler = mock(HandlerExecution.class);

        //when
        HandlerAdaptor foundAdaptor = handlerAdaptorRegistry.getHandlerAdaptors(handler);

        //then
        assertAll(
                () -> assertThat(foundAdaptor).isInstanceOf(HandlerExecutionAdaptor.class),
                () -> assertThat(foundAdaptor).isEqualTo(handlerAdaptor)
        );
    }

    @Test
    @DisplayName("HandlerAdaptorRegistry에서 해당 handler를 처리할 수 있는 handlerAdaptor가 없다면 NoSuchElementException을 반환한다.")
    void getHandlerAdaptor_failWithUnsupportedHandler() {
        //given
        Object handler = mock(Controller.class);

        //when, then
        assertThatThrownBy(() -> handlerAdaptorRegistry.getHandlerAdaptors(handler))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("HandlerAdaptor");
    }
}
