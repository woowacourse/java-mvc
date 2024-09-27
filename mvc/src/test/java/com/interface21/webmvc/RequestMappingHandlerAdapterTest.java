package com.interface21.webmvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingHandlerAdapterTest {

    private RequestMappingHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new RequestMappingHandlerAdapter();
    }

    @DisplayName("HandlerExecution 타입이면 true를 반환한다.")
    @Test
    void givenHandlerExecution_thenReturnTrue() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        boolean canSupport = handlerAdapter.support(handlerExecution);

        Assertions.assertThat(canSupport).isTrue();
    }

    @DisplayName("HandlerExecution 타입이 아니면 false를 반환한다.")
    @Test
    void givenNotHandlerExecution_thenReturnFalse() {
        Controller controller = mock(Controller.class);

        boolean canSupport = handlerAdapter.support(controller);

        Assertions.assertThat(canSupport).isFalse();
    }
}
