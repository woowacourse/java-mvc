package com.techcourse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ManualHandlerMappingTest {

    @Test
    void controllerMappingsBelongToTheirInstance() {
        final var initializedMapping = new ManualHandlerMapping();
        initializedMapping.initialize();

        final var uninitializedMapping = new ManualHandlerMapping();

        assertThat(initializedMapping.getHandler("/")).isNotNull();
        assertThat(uninitializedMapping.getHandler("/")).isNull();
    }
}
