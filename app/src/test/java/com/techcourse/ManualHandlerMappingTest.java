package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    @Test
    void controllerMappingsBelongToTheirInstance() {
        final var initializedMapping = new ManualHandlerMapping();
        initializedMapping.initialize();

        final var uninitializedMapping = new ManualHandlerMapping();
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/");

        assertThat(initializedMapping.getHandler(request)).isNotNull();
        assertThat(uninitializedMapping.getHandler(request)).isNull();
    }
}
