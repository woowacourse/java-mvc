package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerAdapterRegistryTest() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @BeforeEach
    void setUp() {
        HandlerExecutionHandlerAdapter executionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(executionHandlerAdapter);
    }

    @DisplayName("HandlerExecution을 인자로 받으면 HandlerExecutionHandlerAdapter를 반환한다.")
    @Test
    void getHandlerExecutionHandlerAdapter() {
        TestController testController = new TestController();
        Method method = testController.getClass()
                .getDeclaredMethods()[0];
        HandlerExecution handlerExecution = new HandlerExecution(method, testController);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        assertThat(handlerAdapter)
                .isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @DisplayName("요청에 맞는 HandlerAdapter를 찾을 수 없으면 예외로 처리한다.")
    @Test
    void findNothing() {
        Object object = new Object();

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(object))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Handler에 맞는 HandlerAdapter를 찾을 수 없습니다.");
    }
}
