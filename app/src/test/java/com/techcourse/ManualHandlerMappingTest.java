package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualHandlerMappingTest {

    private ManualHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var result = handlerExecution.execute(request, response);

        assertThat(result).isEqualTo("/index.jsp");
    }
}
