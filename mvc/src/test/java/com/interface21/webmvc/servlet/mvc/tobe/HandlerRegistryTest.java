package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HandlerRegistryTest {

    @Test
    void registrationReturnsNewRegistryWithoutChangingOriginal() {
        final var original = new HandlerRegistry();
        final var execution = mock(HandlerExecution.class);

        final var registered = original.register(new HandlerKey("/test", RequestMethod.GET), execution);

        assertThat(original.getHandler("/test", RequestMethod.GET)).isNull();
        assertThat(registered.getHandler("/test", RequestMethod.GET)).isSameAs(execution);
    }
}
