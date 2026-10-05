package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void explicitMappingHasPriorityRegardlessOfRegistrationOrder(final boolean explicitFirst) {
        final var explicit = mock(HandlerExecution.class);
        final var fallback = mock(HandlerExecution.class);
        final var explicitKey = new HandlerKey("/test", RequestMethod.GET);
        final var fallbackKey = new HandlerKey("/test", null);
        final var registry = explicitFirst
                ? new HandlerRegistry().register(explicitKey, explicit).register(fallbackKey, fallback)
                : new HandlerRegistry().register(fallbackKey, fallback).register(explicitKey, explicit);

        assertThat(registry.getHandler("/test", RequestMethod.GET)).isSameAs(explicit);
        assertThat(registry.getHandler("/test", RequestMethod.POST)).isSameAs(fallback);
    }

    @Test
    void failedDuplicateRegistrationPreservesOriginalHandler() {
        final var key = new HandlerKey("/test", RequestMethod.GET);
        final var original = mock(HandlerExecution.class);
        final var registry = new HandlerRegistry().register(key, original);

        assertThatThrownBy(() -> registry.register(key, mock(HandlerExecution.class)))
                .isInstanceOf(IllegalStateException.class);
        assertThat(registry.getHandler("/test", RequestMethod.GET)).isSameAs(original);
    }
}
