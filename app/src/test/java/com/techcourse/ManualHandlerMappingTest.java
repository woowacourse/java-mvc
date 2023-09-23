package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingTest {

    @Test
    void ManualHandlerMapping은_Controller_인스턴스를_반환한다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final ManualHandlerMapping handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
        when(request.getRequestURI()).thenReturn("/login");

        // expected
        assertThat(handlerMapping.getHandler(request)).isInstanceOf(LoginController.class);
    }
}
