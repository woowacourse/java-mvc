package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("핸들러를 찾을 수 있다.")
    @Test
    void addHandlerMappingAndGetHandler() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();

        // when
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // then
        final Object handler = handlerMappingRegistry.getHandler(request).orElseThrow();
        Assertions.assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
