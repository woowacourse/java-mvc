package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.StatefulController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingUnitTest {

    @Test
    void registersAndExecutesTheSuppliedControllerWithoutScanning() throws Exception {
        final var target = spy(new StatefulController());
        final var scanner = mock(ControllerScanner.class);
        when(scanner.scan()).thenReturn(Map.of(StatefulController.class, target));
        final var mapping = new AnnotationHandlerMapping(scanner);
        mapping.initialize();
        final var getRequest = mock(HttpServletRequest.class);
        final var postRequest = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(getRequest.getRequestURI()).thenReturn("/shared-controller");
        when(getRequest.getMethod()).thenReturn("GET");
        when(postRequest.getRequestURI()).thenReturn("/shared-controller");
        when(postRequest.getMethod()).thenReturn("POST");

        final var getHandler = (HandlerExecution) mapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) mapping.getHandler(postRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("count")).isEqualTo(1);
        assertThat(postHandler.handle(postRequest, response).getObject("count")).isEqualTo(2);
        verify(target).get(getRequest, response);
        verify(target).post(postRequest, response);
    }
}
