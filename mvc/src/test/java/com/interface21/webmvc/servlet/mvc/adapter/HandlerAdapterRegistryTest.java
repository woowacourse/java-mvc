package com.interface21.webmvc.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {
    HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry(List.of(
                mock(HandlerAdapter.class)
        ));
    }

    @Test
    void 핸들러를_지원하는_어댑터가_없을_경우_예외를_던진다() {
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(new UnsupportedHandler()))
                .isInstanceOf(IllegalStateException.class);
    }

    private static class UnsupportedHandler {
    }

}
