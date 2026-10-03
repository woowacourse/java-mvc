package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 지원하는_어댑터가_없으면_예외가_발생한다() {
        // given
        HandlerAdapterRegistry registry = HandlerAdapterRegistry.empty();

        assertThatThrownBy(() -> registry.getHandlerAdapter(new Object()))
            .isInstanceOf(HandlerAdapterNotFoundException.class);
    }

}