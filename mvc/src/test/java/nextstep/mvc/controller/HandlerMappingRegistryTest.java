package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    @Test
    void getHandler() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();
        final var request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMappingRegistry.getHandler(request).get();
        final Object controller = handlerExecution.getController();

        assertThat(controller.getClass()).isSameAs(TestController.class);
    }
}
