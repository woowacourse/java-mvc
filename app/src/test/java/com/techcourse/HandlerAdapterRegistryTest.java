package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.annotation.AnnotationHandlerAdapter;
import com.techcourse.controller.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.initialize();
    }

    @DisplayName("Controller 타입의 핸들러의 경우 ManualHandlerAdapter를 반환한다.")
    @Test
    void should_getManualHandlerAdapter_when_givenControllerHandler() {
        // given
        Object handler = new LoginController();

        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(ManualHandlerAdapter.class);
    }

    @DisplayName("HandlerExecution 타입의 핸들러의 경우 AnnotationHandlerAdapter를 반환한다.")
    @Test
    void should_getAnnotationHandlerAdapter_when_givenHandlerExecution() {
        // given
        Object handler = new HandlerExecution(null, null);

        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
