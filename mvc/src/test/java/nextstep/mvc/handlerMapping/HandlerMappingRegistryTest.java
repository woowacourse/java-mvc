package nextstep.mvc.handlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("HandlerMappingRegistry를 통해 핸들러를 찾을 수 있다.")
    void addHandlerMapping() {
        final HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.from();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerMappingRegistry.getHandler(request);
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}