package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.LegacyController;

public class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapter = new ControllerHandlerAdapter();
    }

    @Test
    void 지원하는_HandlerAdapter를_반환한다() {
        // given
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
        Object handler = new LegacyController();

        // when
        HandlerAdapter result = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(result).isEqualTo(handlerAdapter);
    }

    @Test
    void 지원하지_않는_Handler일_경우_예외를_던진다() {
        // given
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
        Object nonSupportedHandler = new Object();

        // when & then
        assertThrows(IllegalStateException.class, () -> handlerAdapterRegistry.getHandlerAdapter(nonSupportedHandler));
    }
}
