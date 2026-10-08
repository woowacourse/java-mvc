package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    @Test
    void resolvesLegacyControllerThroughCommonMapping() throws Exception {
        final var manualMapping = new ManualHandlerMapping();
        manualMapping.initialize();
        final HandlerMapping mapping = manualMapping;
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final var controller = (Controller) mapping.getHandler(request);

        assertThat(controller.execute(request, mock(HttpServletResponse.class)))
                .isEqualTo("/login.jsp");
    }

    @Test
    void returnsNoHandlerForUnmappedRequest() {
        final var manualMapping = new ManualHandlerMapping();
        manualMapping.initialize();
        final HandlerMapping mapping = manualMapping;
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/missing");

        assertThat(mapping.getHandler(request)).isNull();
    }
}
