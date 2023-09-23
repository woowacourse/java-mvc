package com.techcourse;

import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    private static ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

    @BeforeAll
    static void setUp() {
        manualHandlerMapping.initialize();
    }

    @Test
    void request에_따른_handler를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/login");

        assertThat(manualHandlerMapping.getHandler(request)).isInstanceOf(LoginController.class);
    }
}
