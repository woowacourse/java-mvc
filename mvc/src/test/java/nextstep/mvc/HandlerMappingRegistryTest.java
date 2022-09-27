package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void getAnnotationHandler() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        registry.initialize();

        final Object handler = registry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
