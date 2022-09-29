package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 핸들러가_존재할_경우_반환한다() {
        // given
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("slow");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        // when
        final Object handler = handlerMappingRegistry.getHandler(request).orElseThrow();

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void 핸들러가_존재하지_않는_경우_빈옵셔널을_반환한다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getAttribute("id")).thenReturn("slow");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        // when
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler.isEmpty()).isTrue();
    }
}
