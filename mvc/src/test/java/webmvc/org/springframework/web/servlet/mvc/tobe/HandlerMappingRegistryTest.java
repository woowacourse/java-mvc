package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void 정상적으로_초기화되는_것을_확인한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerMappingRegistry = new HandlerMappingRegistry(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();
        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isNotNull();
    }

}
